/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.IntegrationTests.mock;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.redcom.lib.integration.api.client.dadata.dto.APIErrorMessage;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class UT05ApiErrorMessage {

	// Spring Boot Test requires at least one configuration class
	@Configuration
	public static class DummyConfig {
	}

	@Autowired
	private ObjectMapper objectMapper;

	@Before
	public void configureObjectMapper() {
		objectMapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE,
		                    DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
	}


	@Test
	public void errorNull() throws IOException {
		val response = testApiErrorMessage("null");
		assertThat(response, is(nullValue(APIErrorMessage.class)));
	}

	@Test
	public void fieldIsRequired() throws IOException {
		val response = testApiErrorMessage("{ \"data\" : [ \"Field is required\" ] }");
		assertThat(response, hasProperty("data", arrayContaining("Field is required")));
	}

	@Test
	public void emptyList() throws IOException {
		val response = testApiErrorMessage("{ \"detail\" : \"Bad request. Use non empty list.\" }");
		assertThat(response, hasProperty("detail", equalTo("Bad request. Use non empty list.")));
	}

	@Test
	public void notContainsData() throws IOException {
		val response = testApiErrorMessage("{ \"details\" : [ \"Request does not contain data for standartization\" ] }");
		assertThat(response, hasProperty("details",
		                                 arrayContaining("Request does not contain data for standartization")));
	}

	@Test
	public void arbitraryContents() throws IOException {
		val response = testApiErrorMessage("{ \"key1\" : \"value1\", \"key2\" : [ \"value2a\", \"value2b\" ] }");
		assertThat(response, hasProperty("contents", hasEntry(equalTo("key1"), equalTo("value1"))));
		assertThat(response, allOf(hasProperty("contents", hasEntry(equalTo("key1"), equalTo("value1"))),
		                           hasProperty("contents", hasEntry(equalTo("key2"), contains("value2a", "value2b")))));
	}


	private APIErrorMessage testApiErrorMessage(final String source) throws IOException {
		val response = objectMapper.readValue(source, APIErrorMessage.class);
		System.out.println("error: " + response);
		return response;
	}
}
