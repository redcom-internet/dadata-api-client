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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseActions;
import ru.redcom.software.util.integration.api.client.dadata.DaData;
import ru.redcom.software.util.integration.api.client.dadata.dto.Address;

import javax.annotation.PostConstruct;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

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
	DaData dadata(final RestTemplateBuilder restTemplateBuilder) {
		return new DaData(API_KEY, SECRET_KEY, null, restTemplateBuilder);
	}

	private static ResponseActions setupBaseTestServer(final MockRestServiceServer server,
	                                                   final String expectedUri,
	                                                   final HttpMethod requestMethod) {
		return server.expect(requestTo(expectedUri))
		             .andExpect(method(requestMethod))
		             .andExpect(header(HttpHeaders.ACCEPT, containsString(MediaType.APPLICATION_JSON_VALUE)))
		             .andExpect(header(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY)))
		             .andExpect(header("X-Secret", equalTo(SECRET_KEY)));
	}

	static void setupTestServer(final MockRestServiceServer server, final String expectedUri,
	                            final HttpMethod requestMethod, final String responseBody) {
		setupBaseTestServer(server, expectedUri, requestMethod)
				.andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));
	}

	static void setupTestServer(final MockRestServiceServer server, final String expectedUri,
	                            final HttpMethod requestMethod) {
		setupBaseTestServer(server, expectedUri, requestMethod)
				.andRespond(withSuccess());
	}

	static void doTest(final DaData dadata, final MockRestServiceServer server, final String uri, final HttpMethod method,
	                   final String sourceAddress, final String json, final Matcher<Address> matcher) {
		setupTestServer(server, uri, method, json);
		System.out.println("source address: " + sourceAddress);
		final Address a = dadata.cleanAddress(sourceAddress);
		System.out.println("cleaned address: " + a);
		assertThat(a, is(matcher));
	}
}
