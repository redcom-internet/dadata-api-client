/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.redcom.software.util.integration.api.client.dadata.dto.CompositeRequest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static ru.redcom.software.util.integration.api.client.dadata.dto.CompositeRequest.ElementType.*;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class UT99CompositeRequestMapper {

	// Spring Boot Test requires at least one configuration class
	@Configuration
	public static class DummyConfig {
	}

	@Autowired
	private ObjectMapper objectMapper;


	/*
	{
	  "structure": [
		"AS_IS",
		"NAME",
		"ADDRESS",
		"PHONE" ],
	  "data": [
		[ "1",
		  "Федотов Алексей",
		  "Москва, Сухонская улица, 11 кв 89",
		  "8 916 823 3454"
		],
		[ ["2"],
		  ["Иванов", "Сергей Владимирович"],
		  ["мск", "улица свободы", "65", "12"],
		  ["495 663-12-53"]
		],
		[ "3",
		  ["Ольга Павловна", "Ященко"],
		  ["", "Спб, ул Петрозаводская 8", "", ""],
		  "457 07 25"
		]
	  ]
	}
	*/
	@Test
	public void compositeRequestSample() throws JsonProcessingException {
		// @formatter:off
		final CompositeRequest request = CompositeRequest
				.compose(AS_IS, NAME, ADDRESS, PHONE)
					.element()
						.asIs("1")
						.name("Федотов Алексей")
						.address("Москва, Сухонская улица, 11 кв 89")
						.phone("8 916 823 3454")
				.and()
					.element()
						.asIs("2")
						.name("Иванов", "Сергей Владимирович")
						.address("мск", "улица свободы", "65", "12")
						.phone("495 663-12-53")
				.and()
					.element()
						.asIs("3")
						.name("Ольга Павловна", "Ященко")
						.address("", "Спб, ул Петрозаводская 8", "", "")
						.phone("457 07 25")
				.and()
					.build();
		// @formatter:on
		testCompositeRequest(request, "{\n" +
		                              "\t  \"structure\": [\n" +
		                              "\t\t\"AS_IS\",\n" +
		                              "\t\t\"NAME\",\n" +
		                              "\t\t\"ADDRESS\",\n" +
		                              "\t\t\"PHONE\" ],\n" +
		                              "\t  \"data\": [\n" +
		                              "\t\t[ \"1\",\n" +
		                              "\t\t  \"Федотов Алексей\",\n" +
		                              "\t\t  \"Москва, Сухонская улица, 11 кв 89\",\n" +
		                              "\t\t  \"8 916 823 3454\"\n" +
		                              "\t\t],\n" +
		                              "\t\t[ \"2\",\n" +
		                              "\t\t  [\"Иванов\", \"Сергей Владимирович\"],\n" +
		                              "\t\t  [\"мск\", \"улица свободы\", \"65\", \"12\"],\n" +
		                              "\t\t  \"495 663-12-53\"\n" +
		                              "\t\t],\n" +
		                              "\t\t[ \"3\",\n" +
		                              "\t\t  [\"Ольга Павловна\", \"Ященко\"],\n" +
		                              "\t\t  [\"\", \"Спб, ул Петрозаводская 8\", \"\", \"\"],\n" +
		                              "\t\t  \"457 07 25\"\n" +
		                              "\t\t]\n" +
		                              "\t  ]\n" +
		                              "\t}");
	}

	@Test
	public void compositeRequestFull() throws JsonProcessingException {
		// @formatter:off
		final CompositeRequest request = CompositeRequest
				.compose(AS_IS, ADDRESS, BIRTHDATE, EMAIL, NAME, PASSPORT, PHONE, VEHICLE)
					.element()
						.asIs("as is 1")
						.address("address 1")
						.birthDate("birthdate 1")
						.email("email 1")
						.name("name 1")
						.passport("passport 1")
						.phone("phone 1")
						.vehicle("vehicle 1")
				.and()
					.element()
						.asIs("asis 2a", "asis 2b")
						.address("address 2a", "address 2b")
						.birthDate("birthdate 2a", "birthdate 2b")
						.email("email 2a", "email 2b")
						.name("name 2a", "name 2b")
						.passport("passport 2a", "passport 2b")
						.phone("phone 2a", "phone 2b")
						.vehicle("vehicle 2a", "vehicle 2b")
				.and()
					.element()
						.asIs("asis 3")
						.name("name 2a", "name 2b")
				.and()
					.element()
						.asIs("as-is 4a", "as-is 4b", "as-is 4c")
				.and()
					.build();
		// @formatter:on
		testCompositeRequest(request, "{\"structure\":[\"AS_IS\",\"NAME\",\"ADDRESS\",\"BIRTHDATE\",\"PASSPORT\",\"PHONE\",\"EMAIL\",\"VEHICLE\"],\"data\":[[\"as is 1\",\"name 1\",\"address 1\",\"birthdate 1\",\"passport 1\",\"phone 1\",\"email 1\",\"vehicle 1\"],[[\"asis 2a\",\"asis 2b\"],[\"name 2a\",\"name 2b\"],[\"address 2a\",\"address 2b\"],[\"birthdate 2a\",\"birthdate 2b\"],[\"passport 2a\",\"passport 2b\"],[\"phone 2a\",\"phone 2b\"],[\"email 2a\",\"email 2b\"],[\"vehicle 2a\",\"vehicle 2b\"]],[\"asis 3\",[\"name 2a\",\"name 2b\"],null,null,null,null,null,null],[[\"as-is 4a\",\"as-is 4b\",\"as-is 4c\"],null,null,null,null,null,null,null]]}");
	}

	@Test
	public void compositeRequestGaps() throws JsonProcessingException {
		// @formatter:off
		final CompositeRequest request = CompositeRequest
				.compose(AS_IS, ADDRESS, EMAIL)
					.element()
						.asIs("1")
						.address("г.Хабаровск, ул.Ленина, д.73")
						.email("vasya@test.com")
				.and()
					.element()
						.asIs("2")
						.address("г.Новосибирск, Красный проспект, 1")
				.and()
					.element()
						.asIs("3")
						.email("petya@mail.ru")
				.and()
					.element()
						.asIs("4")
				.and()
					.element()
						.email("john@usa.net")
				.and()
					.build();
		// @formatter:on
		testCompositeRequest(request, "{\"structure\":[\"AS_IS\",\"ADDRESS\",\"EMAIL\"],\"data\":[[\"1\",\"г.Хабаровск, ул.Ленина, д.73\",\"vasya@test.com\"],[\"2\",\"г.Новосибирск, Красный проспект, 1\",null],[\"3\",null,\"petya@mail.ru\"],[\"4\",null,null],[null,null,\"john@usa.net\"]]}");
	}

	private void testCompositeRequest(final CompositeRequest request, final String pattern) throws JsonProcessingException {
		final String s = objectMapper.writeValueAsString(request);
		System.out.println("serialized:");
		System.out.println(s);
		assertThat(s, is(sameJSONAs(pattern)));
	}
}
