/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock;

import org.hamcrest.Matcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseActions;
import org.springframework.test.web.client.response.DefaultResponseCreator;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClient;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClientFactory;
import ru.redcom.software.util.integration.api.client.dadata.dto.Address;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@Configuration
@ContextConfiguration(classes = RestTemplateAutoConfiguration.class)
class CommonMock {
	@Value("${dadata.api-key}")
	private String apiKey;
	@Value("${dadata.secret-key}")
	private String secretKey;
	private static String API_KEY;
	private static String SECRET_KEY;


	@PostConstruct
	void init() {
		API_KEY = apiKey;
		SECRET_KEY = secretKey;
	}

	@Bean
	DaDataClient dadata(final RestTemplateBuilder restTemplateBuilder) {
		return DaDataClientFactory.getInstance(API_KEY, SECRET_KEY, null, restTemplateBuilder);
	}

	// TODO expect requestbody ignore whitespace
	private static ResponseActions setupBaseTestServer(final MockRestServiceServer server,
	                                                   final String expectedUri,
	                                                   final HttpMethod requestMethod) {
		return server.expect(requestTo(expectedUri))
		             .andExpect(method(requestMethod))
		             .andExpect(header(HttpHeaders.ACCEPT, containsString(MediaType.APPLICATION_JSON_VALUE)))
		             .andExpect(header(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY)))
		             .andExpect(header("X-Secret", equalTo(SECRET_KEY)));
	}

	static void setupTestServer(@Nonnull final MockRestServiceServer server, @Nonnull final String expectedUri,
	                            @Nonnull final HttpMethod requestMethod, @Nullable final String requestBody,
	                            @Nonnull final HttpStatus status, @Nullable final String responseBody) {
		ResponseActions responseActions = setupBaseTestServer(server, expectedUri, requestMethod);
		if (requestBody != null)
			responseActions = responseActions.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
			                                 .andExpect(content().json(requestBody));
		DefaultResponseCreator responseCreator = withStatus(status);
		if (responseBody != null)
			responseCreator = responseCreator.body(responseBody).contentType(MediaType.APPLICATION_JSON);
		responseActions.andRespond(responseCreator);
	}

	static void setupTestServer(final MockRestServiceServer server, final String expectedUri,
	                            final HttpMethod requestMethod, final String responseBody) {
		setupTestServer(server, expectedUri, requestMethod, null, HttpStatus.OK, responseBody);
	}

	static void setupTestServer(final MockRestServiceServer server, final String expectedUri,
	                            final HttpMethod requestMethod) {
		setupTestServer(server, expectedUri, requestMethod, null, HttpStatus.OK, null);
	}

	static void successTest(final DaDataClient dadata, final String sourceAddress, final Matcher<Address> matcher) {
		System.out.println("source address: " + sourceAddress);
		final Address a = dadata.cleanAddress(sourceAddress);
		System.out.println("cleaned address: " + a);
		assertThat(a, is(matcher));
	}
}
