/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.redcom.software.util.integration.api.client.dadata.dto.APIErrorMessage;
import ru.redcom.software.util.integration.api.client.dadata.dto.Address;
import ru.redcom.software.util.integration.api.client.dadata.dto.Balance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/*
 * Sample query:
 * <pre>
 * 	curl -s -X POST \
 *     -H "Content-Type: application/json" \
 *     -H "Accept: application/json" \
 *     -H "Authorization: Token d0ef3cc03e28295c6946d9f0b5240f844d0f91d0" \
 *     -H "X-Secret: 73254dc8501cb6f5840a2a7a4c4d13ce49061985" \
 *     -d '[ "Хабаровский край, гор. Хабаровск, п. Берёзовка р-н, ул. Заводская, д.123, кв.111" ]' \
 *     https://dadata.ru/api/v2/clean/address | json_pp
 * </pre>
 */
public class DaData {
	private static final String DADATA_API_DEFAULT_BASE_URI = "https://dadata.ru/api/v2";
	private static final String DADADA_API_ENDPOINT_PROFILE_BALANCE = "/profile/balance";
	private static final String DADADA_API_ENDPOINT_STATUS_CLEAN = "/status/CLEAN";
	private static final String DADADA_API_ENDPOINT_CLEAN_ADDRESS = "/clean/address";

	@Nonnull private final String baseUri;
	@Nonnull private final String apiKey;
	@Nonnull private final String secretKey;

	@Nonnull private final RestTemplate restTemplate;


	/**
	 * Конструктор с возможностью задания базового URI сервиса DaData.
	 * <p>Параметры <code>apiKey, secretKey</code> должны быть непустыми, иначе выдаётся исключение
	 * {@link IllegalArgumentException).
	 * Если параметр <code>baseUri</code> null или пустой, будет использован адрес сервиса по умолчанию.
	 * </p>
	 *
	 * @param apiKey    Ключ API
	 * @param secretKey Пароль API
	 * @param baseUri   Строка базового URI
	 */
	@SuppressWarnings("WeakerAccess")
	public DaData(@Nonnull final String apiKey, @Nonnull final String secretKey, @Nullable String baseUri) {
		if (isNullOrEmpty(apiKey))
			throw new IllegalArgumentException("API Key is not set");
		if (isNullOrEmpty(secretKey))
			throw new IllegalArgumentException("Secret Key is not set");
		this.baseUri = !isNullOrEmpty(baseUri) ? baseUri : DADATA_API_DEFAULT_BASE_URI;
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.restTemplate = createRestTemplate();
	}

	/**
	 * Конструктор с базовым URI сервиса DaData по умолчанию.
	 * <p>Параметры <code>apiKey, secretKey</code> должны быть непустыми, иначе выдаётся исключение
	 * {@link IllegalArgumentException).</p>
	 *
	 * @param apiKey    Ключ API
	 * @param secretKey Пароль API
	 */
	public DaData(@Nonnull final String apiKey, @Nonnull final String secretKey) {
		this(apiKey, secretKey, null);
	}


	/**
	 * Проверить доступность сервиса стандартизации.
	 *
	 * @param silent Если true, то не выдавать исключения, только результат
	 *
	 * @return true - доступен, false - нет
	 *
	 * @throws DaDataException При ошибках проверки, если silent = false
	 */
	public boolean checkAvailability(final boolean silent) throws DaDataException {
		try {
			doRequest(DADADA_API_ENDPOINT_STATUS_CLEAN, HttpMethod.GET, null, Void.class);
			return true;
		} catch (DaDataException e) {
			if (silent)
				return false;
			else
				throw e;
		}
	}

	@Nonnull
	public BigDecimal getProfileBalance() throws DaDataException {
		return doRequest(DADADA_API_ENDPOINT_PROFILE_BALANCE, HttpMethod.GET, null, Balance.class)
				.orElseThrow(() -> new IllegalStateException("Empty result from Get Profile Balance request"))
				.getBalance();
	}

	@Nonnull
	public Address cleanAddress(@Nonnull final String source) throws DaDataException {
		if (isNullOrEmpty(source))
			throw new IllegalArgumentException("Address string is empty");
		final Address[] addresses = cleanAddresses(source);
		if (addresses.length > 0)
			return addresses[0];
		else
			throw new DaDataException("Empty response from DaData API");
	}

	@Nonnull
	public Address[] cleanAddresses(@Nonnull final String... sources) throws DaDataException {
		// TODO временно до включения runtime-инструментации аннотаций
		Assert.notNull(sources, "sources is null");
		return doRequest(DADADA_API_ENDPOINT_CLEAN_ADDRESS, HttpMethod.POST, sources, Address[].class).orElse(new Address[0]);
	}

	// TODO implement other API bindings

	// ------------------ Internal -------------------------------------------------------------------------------------

	// Generic method for request a resource and verify response
	// Optional is only to deal with Void response
	@NonNull
	private <S, R> Optional<R> doRequest(@Nonnull final String endpoint, @Nonnull final HttpMethod requestMethod,
	                                     @Nullable final S requestBody, @NonNull final Class<R> responseClazz) {
		try {
			//noinspection ConstantConditions because of missing @Nullable annotation on body single-arg constructor
			final HttpEntity<S> requestEntity = new HttpEntity<>(requestBody);
			final ResponseEntity<R> responseEntity = restTemplate.exchange(endpoint, requestMethod, requestEntity, responseClazz);

			if (responseEntity.getStatusCode() != HttpStatus.OK)
				throw new DaDataException("REST service response code is not success (" + responseEntity.getStatusCode() + ")", responseEntity.getStatusCode().isError());

			// Ignore body on void requests
			if (Void.class.equals(responseClazz))
				return Optional.empty();

			final R response = responseEntity.getBody();
			if (response == null)
				throw new DaDataException("Empty response from REST service");

			return Optional.of(response);
			// Exception hierarchy:
			//  RestClientException
			//      RestClientResponseException         *
			//          HttpStatusCodeException         *
			//              HttpClientErrorException    *
			//              HttpServerErrorException    *
			//          UnknownHttpStatusCodeException  *
			//      ResourceAccessException
			// * - DaDataClientException will be thrown on request errors by template error handler
		} catch (RestClientException e) {
			// I/O error, including marshaling
			throw new DaDataException("REST resource access or message format error", e);
		}
	}

	// Helper method to check a string for null reference or empty condition.
	private boolean isNullOrEmpty(@Nullable final String s) {
		return s == null || s.isEmpty();
	}

	// Create and make-up a REST Template
	@Nonnull
	private RestTemplate createRestTemplate() {
		// Add authentication headers to each request
		final List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new HeaderRequestInterceptor("Authorization", "Token " + this.apiKey));
		interceptors.add(new HeaderRequestInterceptor("X-Secret", this.secretKey));
		// Set JSON message converter parameters
		final Jackson2ObjectMapperBuilder mapperBuilder = new Jackson2ObjectMapperBuilder();
		mapperBuilder.featuresToEnable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE,
		                               DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
		final MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter(mapperBuilder.build());
		// Set custom error handler
		final DaDataClientErrorHandler errorHandler = new DaDataClientErrorHandler(jsonMessageConverter);
		// Build REST template
		final RestTemplateBuilder templateBuilder = new RestTemplateBuilder();
		return templateBuilder.detectRequestFactory(true)
		                      .rootUri(baseUri)
		                      .interceptors(interceptors)
		                      .messageConverters(jsonMessageConverter)
		                      .errorHandler(errorHandler)
		                      .build();
	}

	// Request Interceptor to add credentials headers
	private static class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {
		@Nonnull private final String headerName;
		@Nonnull private final String headerValue;

		HeaderRequestInterceptor(@Nonnull String headerName, @Nonnull String headerValue) {
			this.headerName = headerName;
			this.headerValue = headerValue;
		}

		@Override
		@Nonnull
		public ClientHttpResponse intercept(@Nonnull HttpRequest request, @Nonnull byte[] body,
		                                    @Nonnull ClientHttpRequestExecution execution) throws IOException {
			request.getHeaders().set(headerName, headerValue);
			return execution.execute(request, body);
		}
	}

	// Error handler to convert HTTP Client Request-related errors to DaDataClientException
	private static class DaDataClientErrorHandler extends DefaultResponseErrorHandler {
		private final List<HttpMessageConverter<?>> messageConverters;

		private DaDataClientErrorHandler(@Nonnull final HttpMessageConverter<?>... messageConverters) {
			this.messageConverters = Arrays.asList(messageConverters);
		}

		@Override
		public void handleError(ClientHttpResponse response) throws IOException {
			final int rawStatusCode = response.getRawStatusCode();
			final HttpStatus statusCode = HttpStatus.resolve(rawStatusCode);
			final String statusText = response.getStatusText();

			// Extract error details from request body
			APIErrorMessage errorDetails = null;
			Exception resourceException = null;
			try {
				final HttpMessageConverterExtractor<? extends APIErrorMessage> extractor =
						new HttpMessageConverterExtractor<>(APIErrorMessage.class, this.messageConverters);
				// log.debug("response body: {}", new BufferedReader(new InputStreamReader(response.getBody())).lines().collect(Collectors.joining("\n")));
				errorDetails = extractor.extractData(response);
			} catch (Exception e) {
				resourceException = e;
			}

			// Decode HTTP error
			APIErrorCode errorCode = null;
			String message = "Unknown HTTP error";
			if (statusCode != null) {
				switch (statusCode.series()) {
					case CLIENT_ERROR:
						errorCode = APIErrorCode.fromHttpStatus(statusCode);
						message = errorCode != null ? errorCode.getMessage() : "HTTP Client error";
						break;
					case SERVER_ERROR:
						message = "HTTP Server error";
						break;
				}
			}

			// Get framework exception, just for someone might need it
			Exception nestedException = null;
			try {
				super.handleError(response);
			} catch (Exception e) {
				nestedException = e;
			}

			final DaDataClientException e = new DaDataClientException(message, rawStatusCode, statusText, errorCode, errorDetails, nestedException);
			if (resourceException != null)
				e.addSuppressed(resourceException);
			throw e;
		}
	}
}
