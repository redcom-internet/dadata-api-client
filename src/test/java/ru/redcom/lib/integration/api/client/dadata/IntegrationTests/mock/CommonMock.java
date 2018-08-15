/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.IntegrationTests.mock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseActions;
import org.springframework.test.web.client.response.DefaultResponseCreator;
import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.DaDataClientFactory;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@Configuration
@ContextConfiguration(classes = RestTemplateAutoConfiguration.class)
class CommonMock {
	private static String API_KEY;
	private static String SECRET_KEY;

	CommonMock(@Value("${dadata.api-key}") final String apiKey,
	           @Value("${dadata.secret-key}") final String secretKey) {
		API_KEY = apiKey;
		SECRET_KEY = secretKey;
	}

	// resttemplatebuilder bean is provided by @RestClientTest
	@Bean
	DaDataClient dadata(final RestTemplateBuilder restTemplateBuilder) {
		return DaDataClientFactory.getInstance(API_KEY, SECRET_KEY, null, restTemplateBuilder);
	}

	private static ResponseActions setupBaseTestServer(final MockRestServiceServer server,
	                                                   final String expectedUri,
	                                                   final HttpMethod requestMethod) {
		server.reset();
		return server.expect(requestTo(expectedUri))
		             .andExpect(method(requestMethod))
		             .andExpect(header(HttpHeaders.ACCEPT, containsString(MediaType.APPLICATION_JSON_VALUE)))
		             .andExpect(header(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY)))
		             .andExpect(header("X-Secret", equalTo(SECRET_KEY)));
	}

	static void setupTestServer(@NonNull final MockRestServiceServer server, @NonNull final String expectedUri,
	                            @NonNull final HttpMethod requestMethod, @Nullable final String requestBody,
	                            @NonNull final HttpStatus status, @Nullable final String responseBody) {
		ResponseActions responseActions = setupBaseTestServer(server, expectedUri, requestMethod);
		if (requestBody != null)
			responseActions = responseActions.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			                                 .andExpect(content().json(requestBody));
		DefaultResponseCreator responseCreator = withStatus(status);
		if (responseBody != null)
			responseCreator = responseCreator.contentType(MediaType.APPLICATION_JSON).body(responseBody);
		responseActions.andRespond(responseCreator);
	}

	static void setupTestServer(final MockRestServiceServer server, final String expectedUri,
	                            final HttpMethod requestMethod, @Nullable final String responseBody) {
		setupTestServer(server, expectedUri, requestMethod, null, HttpStatus.OK, responseBody);
	}
}
