/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.redcom.software.util.integration.api.client.dadata.dto.CompositeResponse;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class UT91CompositeResponseMapper {

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
//	@Test
//	public void compositeRequestSample() throws JsonProcessingException {
//		// @formatter:off
//		final CompositeRequest request = CompositeRequest
//				.compose(AS_IS, NAME, ADDRESS, PHONE)
//					.element()
//						.asIs("1")
//						.name("Федотов Алексей")
//						.address("Москва, Сухонская улица, 11 кв 89")
//						.phone("8 916 823 3454")
//				.and()
//					.element()
//						.asIs("2")
//						.name("Иванов", "Сергей Владимирович")
//						.address("мск", "улица свободы", "65", "12")
//						.phone("495 663-12-53")
//				.and()
//					.element()
//						.asIs("3")
//						.name("Ольга Павловна", "Ященко")
//						.address("", "Спб, ул Петрозаводская 8", "", "")
//						.phone("457 07 25")
//				.and()
//					.build();
//		// @formatter:on
//		testCompositeRequest(request, "{\n" +
//		                              "\t  \"structure\": [\n" +
//		                              "\t\t\"AS_IS\",\n" +
//		                              "\t\t\"NAME\",\n" +
//		                              "\t\t\"ADDRESS\",\n" +
//		                              "\t\t\"PHONE\" ],\n" +
//		                              "\t  \"data\": [\n" +
//		                              "\t\t[ \"1\",\n" +
//		                              "\t\t  \"Федотов Алексей\",\n" +
//		                              "\t\t  \"Москва, Сухонская улица, 11 кв 89\",\n" +
//		                              "\t\t  \"8 916 823 3454\"\n" +
//		                              "\t\t],\n" +
//		                              "\t\t[ \"2\",\n" +
//		                              "\t\t  [\"Иванов\", \"Сергей Владимирович\"],\n" +
//		                              "\t\t  [\"мск\", \"улица свободы\", \"65\", \"12\"],\n" +
//		                              "\t\t  \"495 663-12-53\"\n" +
//		                              "\t\t],\n" +
//		                              "\t\t[ \"3\",\n" +
//		                              "\t\t  [\"Ольга Павловна\", \"Ященко\"],\n" +
//		                              "\t\t  [\"\", \"Спб, ул Петрозаводская 8\", \"\", \"\"],\n" +
//		                              "\t\t  \"457 07 25\"\n" +
//		                              "\t\t]\n" +
//		                              "\t  ]\n" +
//		                              "\t}");
//	}

//	@Test
//	public void compositeRequestGaps() throws JsonProcessingException {
//		// @formatter:off
//		final CompositeRequest request = CompositeRequest
//				.compose(AS_IS, ADDRESS, EMAIL)
//					.element()
//						.asIs("1")
//						.address("г.Хабаровск, ул.Ленина, д.73")
//						.email("vasya@test.com")
//				.and()
//					.element()
//						.asIs("2")
//						.address("г.Новосибирск, Красный проспект, 1")
//				.and()
//					.element()
//						.asIs("3")
//						.email("petya@mail.ru")
//				.and()
//					.element()
//						.asIs("4")
//				.and()
//					.element()
//						.email("john@usa.net")
//				.and()
//					.build();
//		// @formatter:on
//		testCompositeRequest(request, "{\"structure\":[\"AS_IS\",\"ADDRESS\",\"EMAIL\"],\"data\":[[\"1\",\"г.Хабаровск, ул.Ленина, д.73\",\"vasya@test.com\"],[\"2\",\"г.Новосибирск, Красный проспект, 1\",null],[\"3\",null,\"petya@mail.ru\"],[\"4\",null,null],[null,null,\"john@usa.net\"]]}");
//	}


	@Test
	public void compositeResponseFull() throws IOException {
		val response = testCompositeResponse("{\"structure\":[\"AS_IS\",\"NAME\",\"ADDRESS\",\"BIRTHDATE\",\"PASSPORT\",\"PHONE\",\"EMAIL\",\"VEHICLE\"],\"data\":[[{\"source\":\"as is 1\"},{\"source\":\"name 1\",\"result\":null,\"result_genitive\":null,\"result_dative\":null,\"result_ablative\":null,\"surname\":null,\"name\":null,\"patronymic\":null,\"gender\":\"НД\",\"qc\":1},{\"source\":\"address 1\",\"result\":null,\"postal_code\":null,\"country\":null,\"region_fias_id\":null,\"region_kladr_id\":null,\"region_with_type\":null,\"region_type\":null,\"region_type_full\":null,\"region\":null,\"area_fias_id\":null,\"area_kladr_id\":null,\"area_with_type\":null,\"area_type\":null,\"area_type_full\":null,\"area\":null,\"city_fias_id\":null,\"city_kladr_id\":null,\"city_with_type\":null,\"city_type\":null,\"city_type_full\":null,\"city\":null,\"city_area\":null,\"city_district_fias_id\":null,\"city_district_kladr_id\":null,\"city_district_with_type\":null,\"city_district_type\":null,\"city_district_type_full\":null,\"city_district\":null,\"settlement_fias_id\":null,\"settlement_kladr_id\":null,\"settlement_with_type\":null,\"settlement_type\":null,\"settlement_type_full\":null,\"settlement\":null,\"street_fias_id\":null,\"street_kladr_id\":null,\"street_with_type\":null,\"street_type\":null,\"street_type_full\":null,\"street\":null,\"house_fias_id\":null,\"house_kladr_id\":null,\"house_type\":null,\"house_type_full\":null,\"house\":null,\"block_type\":null,\"block_type_full\":null,\"block\":null,\"flat_type\":null,\"flat_type_full\":null,\"flat\":null,\"flat_area\":null,\"square_meter_price\":null,\"flat_price\":null,\"postal_box\":null,\"fias_id\":null,\"fias_code\":null,\"fias_level\":\"-1\",\"fias_actuality_state\":\"0\",\"kladr_id\":null,\"capital_marker\":\"0\",\"okato\":null,\"oktmo\":null,\"tax_office\":null,\"tax_office_legal\":null,\"timezone\":null,\"geo_lat\":null,\"geo_lon\":null,\"beltway_hit\":null,\"beltway_distance\":null,\"qc_geo\":5,\"qc_complete\":1,\"qc_house\":10,\"qc\":1,\"unparsed_parts\":\"АДДРЕСС, 1\",\"metro\":null},{\"source\":\"birthdate 1\",\"birthdate\":null,\"qc\":1},{\"source\":\"passport 1\",\"series\":null,\"number\":null,\"qc\":1},{\"source\":\"phone 1\",\"type\":\"Неизвестный\",\"phone\":null,\"country_code\":null,\"city_code\":null,\"number\":null,\"extension\":null,\"provider\":null,\"region\":null,\"timezone\":null,\"qc_conflict\":0,\"qc\":1},{\"source\":\"email 1\",\"email\":null,\"qc\":1},{\"source\":\"vehicle 1\",\"result\":\"TRABANT 1.1\",\"brand\":\"TRABANT\",\"model\":\"1.1\",\"qc\":0}],[{\"source\":\"asis 2a asis 2b\"},{\"source\":\"name 2a name 2b\",\"result\":\"Б\",\"result_genitive\":null,\"result_dative\":null,\"result_ablative\":null,\"surname\":null,\"name\":\"Б\",\"patronymic\":null,\"gender\":\"НД\",\"qc\":1},{\"source\":\"address 2a,address 2b\",\"result\":null,\"postal_code\":null,\"country\":null,\"region_fias_id\":null,\"region_kladr_id\":null,\"region_with_type\":null,\"region_type\":null,\"region_type_full\":null,\"region\":null,\"area_fias_id\":null,\"area_kladr_id\":null,\"area_with_type\":null,\"area_type\":null,\"area_type_full\":null,\"area\":null,\"city_fias_id\":null,\"city_kladr_id\":null,\"city_with_type\":null,\"city_type\":null,\"city_type_full\":null,\"city\":null,\"city_area\":null,\"city_district_fias_id\":null,\"city_district_kladr_id\":null,\"city_district_with_type\":null,\"city_district_type\":null,\"city_district_type_full\":null,\"city_district\":null,\"settlement_fias_id\":null,\"settlement_kladr_id\":null,\"settlement_with_type\":null,\"settlement_type\":null,\"settlement_type_full\":null,\"settlement\":null,\"street_fias_id\":null,\"street_kladr_id\":null,\"street_with_type\":null,\"street_type\":null,\"street_type_full\":null,\"street\":null,\"house_fias_id\":null,\"house_kladr_id\":null,\"house_type\":null,\"house_type_full\":null,\"house\":null,\"block_type\":null,\"block_type_full\":null,\"block\":null,\"flat_type\":null,\"flat_type_full\":null,\"flat\":null,\"flat_area\":null,\"square_meter_price\":null,\"flat_price\":null,\"postal_box\":null,\"fias_id\":null,\"fias_code\":null,\"fias_level\":\"-1\",\"fias_actuality_state\":\"0\",\"kladr_id\":null,\"capital_marker\":\"0\",\"okato\":null,\"oktmo\":null,\"tax_office\":null,\"tax_office_legal\":null,\"timezone\":null,\"geo_lat\":null,\"geo_lon\":null,\"beltway_hit\":null,\"beltway_distance\":null,\"qc_geo\":5,\"qc_complete\":1,\"qc_house\":10,\"qc\":1,\"unparsed_parts\":\"АДДРЕСС, 2, А, АДДРЕСС, 2, Б\",\"metro\":null},{\"source\":\"birthdate 2a birthdate 2b\",\"birthdate\":null,\"qc\":1},{\"source\":\"passport 2a passport 2b\",\"series\":null,\"number\":null,\"qc\":1},{\"source\":\"phone 2a phone 2b\",\"type\":\"Неизвестный\",\"phone\":null,\"country_code\":null,\"city_code\":null,\"number\":null,\"extension\":null,\"provider\":null,\"region\":null,\"timezone\":null,\"qc_conflict\":0,\"qc\":1},{\"source\":\"email 2a email 2b\",\"email\":null,\"qc\":1},{\"source\":\"vehicle 2a vehicle 2b\",\"result\":null,\"brand\":null,\"model\":null,\"qc\":1}],[{\"source\":\"asis 3\"},{\"source\":\"name 2a name 2b\",\"result\":\"Б\",\"result_genitive\":null,\"result_dative\":null,\"result_ablative\":null,\"surname\":null,\"name\":\"Б\",\"patronymic\":null,\"gender\":\"НД\",\"qc\":1},{\"source\":null,\"result\":null,\"postal_code\":null,\"country\":null,\"region_fias_id\":null,\"region_kladr_id\":null,\"region_with_type\":null,\"region_type\":null,\"region_type_full\":null,\"region\":null,\"area_fias_id\":null,\"area_kladr_id\":null,\"area_with_type\":null,\"area_type\":null,\"area_type_full\":null,\"area\":null,\"city_fias_id\":null,\"city_kladr_id\":null,\"city_with_type\":null,\"city_type\":null,\"city_type_full\":null,\"city\":null,\"city_area\":null,\"city_district_fias_id\":null,\"city_district_kladr_id\":null,\"city_district_with_type\":null,\"city_district_type\":null,\"city_district_type_full\":null,\"city_district\":null,\"settlement_fias_id\":null,\"settlement_kladr_id\":null,\"settlement_with_type\":null,\"settlement_type\":null,\"settlement_type_full\":null,\"settlement\":null,\"street_fias_id\":null,\"street_kladr_id\":null,\"street_with_type\":null,\"street_type\":null,\"street_type_full\":null,\"street\":null,\"house_fias_id\":null,\"house_kladr_id\":null,\"house_type\":null,\"house_type_full\":null,\"house\":null,\"block_type\":null,\"block_type_full\":null,\"block\":null,\"flat_type\":null,\"flat_type_full\":null,\"flat\":null,\"flat_area\":null,\"square_meter_price\":null,\"flat_price\":null,\"postal_box\":null,\"fias_id\":null,\"fias_code\":null,\"fias_level\":\"-1\",\"fias_actuality_state\":\"0\",\"kladr_id\":null,\"capital_marker\":\"0\",\"okato\":null,\"oktmo\":null,\"tax_office\":null,\"tax_office_legal\":null,\"timezone\":null,\"geo_lat\":null,\"geo_lon\":null,\"beltway_hit\":null,\"beltway_distance\":null,\"qc_geo\":5,\"qc_complete\":1,\"qc_house\":10,\"qc\":2,\"unparsed_parts\":null,\"metro\":null},{\"source\":null,\"birthdate\":null,\"qc\":2},{\"source\":null,\"series\":null,\"number\":null,\"qc\":2},{\"source\":null,\"type\":null,\"phone\":null,\"country_code\":null,\"city_code\":null,\"number\":null,\"extension\":null,\"provider\":null,\"region\":null,\"timezone\":null,\"qc_conflict\":0,\"qc\":2},{\"source\":null,\"email\":null,\"qc\":2},{\"source\":null,\"result\":null,\"brand\":null,\"model\":null,\"qc\":2}],[{\"source\":\"as-is 4a as-is 4b as-is 4c\"},{\"source\":null,\"result\":null,\"result_genitive\":null,\"result_dative\":null,\"result_ablative\":null,\"surname\":null,\"name\":null,\"patronymic\":null,\"gender\":\"НД\",\"qc\":2},{\"source\":null,\"result\":null,\"postal_code\":null,\"country\":null,\"region_fias_id\":null,\"region_kladr_id\":null,\"region_with_type\":null,\"region_type\":null,\"region_type_full\":null,\"region\":null,\"area_fias_id\":null,\"area_kladr_id\":null,\"area_with_type\":null,\"area_type\":null,\"area_type_full\":null,\"area\":null,\"city_fias_id\":null,\"city_kladr_id\":null,\"city_with_type\":null,\"city_type\":null,\"city_type_full\":null,\"city\":null,\"city_area\":null,\"city_district_fias_id\":null,\"city_district_kladr_id\":null,\"city_district_with_type\":null,\"city_district_type\":null,\"city_district_type_full\":null,\"city_district\":null,\"settlement_fias_id\":null,\"settlement_kladr_id\":null,\"settlement_with_type\":null,\"settlement_type\":null,\"settlement_type_full\":null,\"settlement\":null,\"street_fias_id\":null,\"street_kladr_id\":null,\"street_with_type\":null,\"street_type\":null,\"street_type_full\":null,\"street\":null,\"house_fias_id\":null,\"house_kladr_id\":null,\"house_type\":null,\"house_type_full\":null,\"house\":null,\"block_type\":null,\"block_type_full\":null,\"block\":null,\"flat_type\":null,\"flat_type_full\":null,\"flat\":null,\"flat_area\":null,\"square_meter_price\":null,\"flat_price\":null,\"postal_box\":null,\"fias_id\":null,\"fias_code\":null,\"fias_level\":\"-1\",\"fias_actuality_state\":\"0\",\"kladr_id\":null,\"capital_marker\":\"0\",\"okato\":null,\"oktmo\":null,\"tax_office\":null,\"tax_office_legal\":null,\"timezone\":null,\"geo_lat\":null,\"geo_lon\":null,\"beltway_hit\":null,\"beltway_distance\":null,\"qc_geo\":5,\"qc_complete\":1,\"qc_house\":10,\"qc\":2,\"unparsed_parts\":null,\"metro\":null},{\"source\":null,\"birthdate\":null,\"qc\":2},{\"source\":null,\"series\":null,\"number\":null,\"qc\":2},{\"source\":null,\"type\":null,\"phone\":null,\"country_code\":null,\"city_code\":null,\"number\":null,\"extension\":null,\"provider\":null,\"region\":null,\"timezone\":null,\"qc_conflict\":0,\"qc\":2},{\"source\":null,\"email\":null,\"qc\":2},{\"source\":null,\"result\":null,\"brand\":null,\"model\":null,\"qc\":2}]]}");
		assertThat(response, is(notNullValue()));
		// TODO продолжить
	}

	private CompositeResponse testCompositeResponse(final String source) throws IOException {
		val response = objectMapper.readValue(source, CompositeResponse.class);
		System.out.println("response: " + response);
		return response;
	}
}
