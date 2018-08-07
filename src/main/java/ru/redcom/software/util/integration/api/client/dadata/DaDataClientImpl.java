/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata;

import com.fasterxml.jackson.databind.DeserializationFeature;
import lombok.val;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.redcom.software.util.integration.api.client.dadata.dto.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

/**
 * DaData API implementation using the Spring REST Template.
 */
class DaDataClientImpl implements DaDataClient {
	private static final String DADATA_API_DEFAULT_BASE_URI = "https://dadata.ru/api/v2";
	private static final String DADADA_API_ENDPOINT_PROFILE_BALANCE = "/profile/balance";
	private static final String DADADA_API_ENDPOINT_STATUS_CLEAN = "/status/CLEAN";
	private static final String DADADA_API_ENDPOINT_CLEAN_ADDRESS = "/clean/address";
	private static final String DADADA_API_ENDPOINT_CLEAN_PHONE = "/clean/phone";
	private static final String DADADA_API_ENDPOINT_CLEAN_PASSPORT = "/clean/passport";
	private static final String DADADA_API_ENDPOINT_CLEAN_NAME = "/clean/name";
	private static final String DADADA_API_ENDPOINT_CLEAN_EMAIL = "/clean/email";
	private static final String DADADA_API_ENDPOINT_CLEAN_BIRTHDATE = "/clean/birthdate";
	private static final String DADADA_API_ENDPOINT_CLEAN_VEHICLE = "/clean/vehicle";
	private static final String DADADA_API_ENDPOINT_CLEAN_COMPOSITE = "/clean";

	@NonNull private final String baseUri;
	@NonNull private final String apiKey;
	@NonNull private final String secretKey;

	@NonNull private final RestTemplateBuilder restTemplateBuilder;
	@NonNull private final RestTemplate restTemplate;


	// Constructor
	DaDataClientImpl(@NonNull final String apiKey, @NonNull final String secretKey, @Nullable final String baseUri,
	                 @NonNull RestTemplateBuilder restTemplateBuilder) {
		this.baseUri = StringUtils.hasText(baseUri) ? baseUri : DADATA_API_DEFAULT_BASE_URI;
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.restTemplateBuilder = restTemplateBuilder;
		this.restTemplate = createRestTemplate();
	}


	// Check DaData API service availability
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

	// Get account profile balance
	@NonNull
	@Override
	public BigDecimal getProfileBalance() throws DaDataException {
		return doRequest(DADADA_API_ENDPOINT_PROFILE_BALANCE, HttpMethod.GET, null, Balance.class)
				.orElseThrow(() -> new IllegalStateException("Empty result from Get Profile Balance request"))
				.getBalance();
	}

	// ------- Address -------------------------------------------------------------------------------------------------

	@NonNull
	@Override
	public Address cleanAddress(@NonNull final String source) throws DaDataException {
		Assert.isTrue(StringUtils.hasText(source), "Address string is empty");
		return getFirstEntry(cleanAddresses(source));
	}

	@NonNull
	@Override
	public Address[] cleanAddresses(@NonNull final String... sources) throws DaDataException {
		Assert.notNull(sources, "Address sources is null");
		return doRequest(DADADA_API_ENDPOINT_CLEAN_ADDRESS, HttpMethod.POST, sources, Address[].class).orElse(new Address[0]);
	}

	// ------- Phone number --------------------------------------------------------------------------------------------

	@NonNull
	@Override
	public Phone cleanPhone(@NonNull final String source) throws DaDataException {
		Assert.isTrue(StringUtils.hasText(source), "Phone number string is empty");
		return getFirstEntry(cleanPhones(source));
	}

	@NonNull
	@Override
	public Phone[] cleanPhones(@NonNull final String... sources) throws DaDataException {
		Assert.notNull(sources, "Phone number sources is null");
		return doRequest(DADADA_API_ENDPOINT_CLEAN_PHONE, HttpMethod.POST, sources, Phone[].class).orElse(new Phone[0]);
	}

	// ------- Passport ------------------------------------------------------------------------------------------------

	@NonNull
	@Override
	public Passport cleanPassport(@NonNull final String source) throws DaDataException {
		Assert.isTrue(StringUtils.hasText(source), "Passport number string is empty");
		return getFirstEntry(cleanPassports(source));
	}

	@NonNull
	@Override
	public Passport[] cleanPassports(@NonNull final String... sources) throws DaDataException {
		Assert.notNull(sources, "Passport number sources is null");
		return doRequest(DADADA_API_ENDPOINT_CLEAN_PASSPORT, HttpMethod.POST, sources, Passport[].class).orElse(new Passport[0]);
	}

	// ------- Name (FIO) ----------------------------------------------------------------------------------------------

	@NonNull
	@Override
	public Name cleanName(@NonNull final String source) throws DaDataException {
		Assert.isTrue(StringUtils.hasText(source), "Name string is empty");
		return getFirstEntry(cleanNames(source));
	}

	@NonNull
	@Override
	public Name[] cleanNames(@NonNull final String... sources) throws DaDataException {
		Assert.notNull(sources, "Name sources is null");
		return doRequest(DADADA_API_ENDPOINT_CLEAN_NAME, HttpMethod.POST, sources, Name[].class).orElse(new Name[0]);
	}

	// ------- Email ---------------------------------------------------------------------------------------------------

	@NonNull
	@Override
	public Email cleanEmail(@NonNull final String source) throws DaDataException {
		Assert.isTrue(StringUtils.hasText(source), "Email string is empty");
		return getFirstEntry(cleanEmails(source));
	}

	@NonNull
	@Override
	public Email[] cleanEmails(@NonNull final String... sources) throws DaDataException {
		Assert.notNull(sources, "Email sources is null");
		return doRequest(DADADA_API_ENDPOINT_CLEAN_EMAIL, HttpMethod.POST, sources, Email[].class).orElse(new Email[0]);
	}

	// ------- Birthdate -----------------------------------------------------------------------------------------------

	@NonNull
	@Override
	public BirthDate cleanBirthDate(@NonNull final String source) throws DaDataException {
		Assert.isTrue(StringUtils.hasText(source), "Birthdate string is empty");
		return getFirstEntry(cleanBirthDates(source));
	}

	@NonNull
	@Override
	public BirthDate[] cleanBirthDates(@NonNull final String... sources) throws DaDataException {
		Assert.notNull(sources, "Birthdate sources is null");
		return doRequest(DADADA_API_ENDPOINT_CLEAN_BIRTHDATE, HttpMethod.POST, sources, BirthDate[].class).orElse(new BirthDate[0]);
	}

	// ------- Vehicle -----------------------------------------------------------------------------------------------

	@NonNull
	@Override
	public Vehicle cleanVehicle(@NonNull final String source) throws DaDataException {
		Assert.isTrue(StringUtils.hasText(source), "Vehicle string is empty");
		return getFirstEntry(cleanVehicles(source));
	}

	@NonNull
	@Override
	public Vehicle[] cleanVehicles(@NonNull final String... sources) throws DaDataException {
		Assert.notNull(sources, "Vehicle sources is null");
		return doRequest(DADADA_API_ENDPOINT_CLEAN_VEHICLE, HttpMethod.POST, sources, Vehicle[].class).orElse(new Vehicle[0]);
	}

	// ------- Composite -----------------------------------------------------------------------------------------------

	@NonNull
	@Override
	public CompositeResponse cleanComposite(@NonNull final CompositeRequest source) throws DaDataException {
		Assert.notNull(source, "Composite request is null");
		return doRequest(DADADA_API_ENDPOINT_CLEAN_COMPOSITE, HttpMethod.POST, source, CompositeResponse.class).orElse(CompositeResponse.EMPTY_RESPONSE);
	}

	// ------------------ Internal -------------------------------------------------------------------------------------

	// Get first entry from array or throw an exception if none
	@NonNull
	private <T> T getFirstEntry(final T[] entries) {
		if (entries.length > 0)
			return entries[0];
		else
			throw new DaDataException("Empty response from DaData API");
	}

	// =================================================================================================================

	// Generic method for request a resource and verify response
	// Optional is only to deal with Void response
	@NonNull
	private <S, R> Optional<R> doRequest(@NonNull final String endpoint, @NonNull final HttpMethod requestMethod,
	                                     @Nullable final S requestBody, @NonNull final Class<R> responseClazz) {
		try {
			//noinspection ConstantConditions because of missing @Nullable annotation on body single-arg constructor
			val requestEntity = new HttpEntity<S>(requestBody);
			val responseEntity = restTemplate.<R>exchange(endpoint, requestMethod, requestEntity, responseClazz);

			if (responseEntity.getStatusCode() != HttpStatus.OK)
				throw new DaDataException("REST service response code is not success (" + responseEntity.getStatusCode() + ")", responseEntity.getStatusCode().isError());

			// Ignore response body on void requests
			if (Void.class.equals(responseClazz))
				return Optional.empty();

			val response = responseEntity.getBody();
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
			throw new DaDataException("REST resource access or message format error", e, false);
		}
	}

	// Create and make-up a REST Template
	@NonNull
	private RestTemplate createRestTemplate() {
		// Set interceptors to add authentication headers to each request
		val interceptors = Arrays.<ClientHttpRequestInterceptor>asList(
				new HeaderRequestInterceptor(HttpHeaders.AUTHORIZATION, "Token " + this.apiKey),
				new HeaderRequestInterceptor("X-Secret", this.secretKey));
		// Set JSON message converter parameters
		val mapperBuilder = new Jackson2ObjectMapperBuilder();
		mapperBuilder.featuresToEnable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE,
		                               DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
		val jsonMessageConverter = new MappingJackson2HttpMessageConverter(mapperBuilder.build());
		// Set custom error handler
		val errorHandler = new ClientErrorHandler(jsonMessageConverter);
		// Build REST template
		return restTemplateBuilder.detectRequestFactory(true)
		                          .rootUri(baseUri)
		                          .interceptors(interceptors)
		                          .messageConverters(jsonMessageConverter)
		                          .errorHandler(errorHandler)
		                          .build();
	}
}
