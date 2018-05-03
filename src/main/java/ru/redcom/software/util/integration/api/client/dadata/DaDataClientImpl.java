/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.redcom.software.util.integration.api.client.dadata.dto.Address;
import ru.redcom.software.util.integration.api.client.dadata.dto.Balance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
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
class DaDataClientImpl implements DaDataClient {
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
	 * {@link IllegalArgumentException}.
	 * Если параметр <code>baseUri</code> null или пустой, будет использован адрес сервиса по умолчанию.
	 * </p>
	 * <p>
	 * Конструктор позволяет задать объект REST Template Builder, что может оказаться полезным в среде Spring Boot.
	 * </p>
	 *
	 * @param apiKey    Ключ API
	 * @param secretKey Пароль API
	 * @param baseUri   Строка базового URI
	 */
	DaDataClientImpl(@Nonnull final String apiKey, @Nonnull final String secretKey, @Nullable String baseUri, @NonNull RestTemplateBuilder restTemplateBuilder) {
		this.baseUri = StringUtils.hasText(baseUri) ? baseUri : DADATA_API_DEFAULT_BASE_URI;
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.restTemplate = createRestTemplate(restTemplateBuilder);
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
	@Override
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

	@Override
	@Nonnull
	public BigDecimal getProfileBalance() throws DaDataException {
		return doRequest(DADADA_API_ENDPOINT_PROFILE_BALANCE, HttpMethod.GET, null, Balance.class)
				.orElseThrow(() -> new IllegalStateException("Empty result from Get Profile Balance request"))
				.getBalance();
	}

	@Override
	@Nonnull
	public Address cleanAddress(@Nonnull final String source) throws DaDataException {
		Assert.isTrue(StringUtils.hasText(source), "Address string is empty");
		final Address[] addresses = cleanAddresses(source);
		if (addresses.length > 0)
			return addresses[0];
		else
			throw new DaDataException("Empty response from DaData API");
	}

	@Override
	@Nonnull
	public Address[] cleanAddresses(@Nonnull final String... sources) throws DaDataException {
		Assert.notNull(sources, "Address sources is null");
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

	// Create and make-up a REST Template
	@Nonnull
	private RestTemplate createRestTemplate(@NonNull final RestTemplateBuilder restTemplateBuilder) {
		// Add authentication headers to each request
		final List<ClientHttpRequestInterceptor> interceptors = Arrays.asList(
				new HeaderRequestInterceptor(HttpHeaders.AUTHORIZATION, "Token " + this.apiKey),
				new HeaderRequestInterceptor("X-Secret", this.secretKey));
		// Set JSON message converter parameters
		final Jackson2ObjectMapperBuilder mapperBuilder = new Jackson2ObjectMapperBuilder();
		mapperBuilder.featuresToEnable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE,
		                               DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
		final MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter(mapperBuilder.build());
		// Set custom error handler
		final ClientErrorHandler errorHandler = new ClientErrorHandler(jsonMessageConverter);
		// Build REST template
		return restTemplateBuilder.detectRequestFactory(true)
		                          .rootUri(baseUri)
		                          .interceptors(interceptors)
		                          .messageConverters(jsonMessageConverter)
		                          .errorHandler(errorHandler)
		                          .build();
	}
}
