/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.redcom.software.util.integration.api.client.dadata.dto.*;

import java.io.IOException;
import java.util.stream.StreamSupport;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessAddress.SampleAddresses;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessBirthDate.SampleBirthDates;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessComposite.*;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessEmail.SampleEmails;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessName.SampleNames;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessPassport.SamplePassports;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessPhone.SamplePhones;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessVehicle.SampleVehicles;
import static ru.redcom.software.util.integration.api.client.dadata.dto.CompositeElementType.*;

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

	@Before
	public void configureObjectMapper() {
		objectMapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE,
		                    DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
	}


	@Test
	public void compositeResponseNull() throws IOException {
		val response = testCompositeResponse("null");
		assertThat(response, is(nullValue(CompositeResponse.class)));
	}

	@Test
	public void compositeResponseSimple() throws IOException {
		val response = testCompositeResponse("{\"structure\":[\"AS_IS\"],\"data\":[[{\"source\":\"as is 1\"}]]}");
		assertThat(response, hasProperty("structure", contains(AS_IS)));
		assertThat(response, hasProperty("data",
		                                 contains(pojo(CompositeResponse.Record.class)
				                                          .withProperty("asIs", asisMatcher("as is 1")))));
	}

	@Test
	public void compositeResponseFull() throws IOException {
		// bombastic sample captured from live processing of composite request with unrecognized content
		val response = testCompositeResponse(MOCK_RESPONSE_FULL_JSON);
		assertThat(response, hasProperty("structure", contains(AS_IS, NAME, ADDRESS, BIRTHDATE, PASSPORT, PHONE, EMAIL, VEHICLE)));
		// just a simple hash-style check, it's boring to compose matchers for all the payload
		assertThat(response, hasToString(MOCK_RESPONSE_FULL_STRING));
	}

	@Test
	public void dataIsNull() throws IOException {
		val response = testCompositeResponse("{\"structure\":[\"AS_IS\"],\"data\":null}");
		assertThat(response, hasProperty("structure", contains(AS_IS)));
		assertThat(response, hasProperty("data", is(emptyCollectionOf(CompositeResponse.Record.class))));
	}

	@Test
	public void dataIsEmpty() throws IOException {
		val response = testCompositeResponse("{\"structure\":[\"AS_IS\"],\"data\":[]}");
		assertThat(response, hasProperty("structure", contains(AS_IS)));
		assertThat(response, hasProperty("data", is(emptyCollectionOf(CompositeResponse.Record.class))));
	}

	@Test
	public void iterableIsEmpty() throws IOException {
		val response = testCompositeResponse("{\"structure\":[\"AS_IS\"],\"data\":[]}");
		assertThat(response, hasProperty("structure", contains(AS_IS)));
		assertThat(response, is(emptyIterable()));
	}

	@Test
	public void recordIsEmpty() throws IOException {
		val response = testCompositeResponse("{\"structure\":[\"AS_IS\"],\"data\":[[{\"source\":\"as is 1\"}],[]]}");
		assertThat(response, hasProperty("structure", contains(AS_IS)));
		//noinspection unchecked
		assertThat(response, hasProperty("data",
		                                 contains(pojo(CompositeResponse.Record.class).withProperty("asIs", asisMatcher("as is 1")),
		                                          pojo(CompositeResponse.Record.class).where("isEmpty", is(true)))));
	}

	@Test
	public void recordIsNull() throws IOException {
		val response = testCompositeResponse("{\"structure\":[\"AS_IS\"],\"data\":[[{\"source\":\"as is 1\"}],[null]]}");
		assertThat(response, hasProperty("structure", contains(AS_IS)));
		//noinspection unchecked
		assertThat(response, hasProperty("data",
		                                 contains(pojo(CompositeResponse.Record.class).withProperty("asIs", asisMatcher("as is 1")),
		                                          pojo(CompositeResponse.Record.class).withProperty("asIs", is(nullValue(AsIs.class))))));
	}

	@Test
	public void elementIsNull() throws IOException {
		val response = testCompositeResponse("{\"structure\":[\"AS_IS\",\"PHONE\"],\"data\":[[{\"source\":\"as is 1\"},null]]}");
		assertThat(response, hasProperty("structure", contains(AS_IS, PHONE)));
		//noinspection unchecked
		assertThat(response, hasProperty("data",
		                                 contains(pojo(CompositeResponse.Record.class).withProperty("asIs", asisMatcher("as is 1"))
		                                                                              .withProperty("phone", is(nullValue(Phone.class))))));
	}

	@Test
	public void compositeResponseFullOfSamples() throws IOException {
		val response = testCompositeResponse(SampleComposite.SAMPLES_FULL.getResponseBody());
		assertThat(response, hasProperty("structure", hasItems(AS_IS, NAME, ADDRESS, BIRTHDATE, PASSPORT, PHONE, EMAIL, VEHICLE)));
		//noinspection unchecked
		assertThat(response, hasProperty("data",
		                                 contains(pojo(CompositeResponse.Record.class)
				                                          .withProperty("asIs", asisMatcher("as is sample 1"))
				                                          .withProperty("address", SampleAddresses.KHABAROVSK_1.getMatcher())
				                                          .withProperty("birthDate", SampleBirthDates.VALID_1.getMatcher())
				                                          .withProperty("email", SampleEmails.VALID.getMatcher())
				                                          .withProperty("name", SampleNames.MALE_1.getMatcher())
				                                          .withProperty("passport", SamplePassports.VALID_1.getMatcher())
				                                          .withProperty("phone", SamplePhones.KHABAROVSK_1.getMatcher())
				                                          .withProperty("vehicle", SampleVehicles.VALID_1.getMatcher()),
		                                          pojo(CompositeResponse.Record.class)
				                                          .withProperty("asIs", asisMatcher("as is sample 2"))
				                                          .withProperty("address", SampleAddresses.MOSCOW_1.getMatcher())
				                                          .withProperty("birthDate", SampleBirthDates.VALID_2.getMatcher())
				                                          .withProperty("email", SampleEmails.INSTANT.getMatcher())
				                                          .withProperty("name", SampleNames.FEMALE_1.getMatcher())
				                                          .withProperty("passport", SamplePassports.VOID_1.getMatcher())
				                                          .withProperty("phone", SamplePhones.MOSCOW_1.getMatcher())
				                                          .withProperty("vehicle", SampleVehicles.VALID_2.getMatcher()),
		                                          pojo(CompositeResponse.Record.class)
				                                          .withProperty("asIs", asisMatcher("as is sample 3"))
				                                          .withProperty("address", SampleAddresses.NOVOSIB_1.getMatcher())
				                                          .withProperty("birthDate", SampleBirthDates.VALID_3.getMatcher())
				                                          .withProperty("email", SampleEmails.CORRECTED.getMatcher())
				                                          .withProperty("name", SampleNames.CHINESE.getMatcher())
				                                          .withProperty("passport", SamplePassports.BAD_FORMAT_1.getMatcher())
				                                          .withProperty("phone", SamplePhones.NOVOSIB_MOBILE_1.getMatcher())
				                                          .withProperty("vehicle", SampleVehicles.VALID_3.getMatcher())
		                                         )));
	}

	@Test
	public void compositeResponseFullOfSamplesIterable() throws IOException {
		val response = testCompositeResponse(SampleComposite.SAMPLES_FULL.getResponseBody());
		assertThat(response, SampleComposite.SAMPLES_FULL.getResponseMatcher());
	}

	@Test
	public void compositeResponseFullOfSamplesGetters() throws IOException {
		val response = testCompositeResponse(SampleComposite.SAMPLES_FULL.getResponseBody());
		assertThat(response, is(notNullValue(CompositeResponse.class)));
		val structure = response.getStructure();
		assertThat(structure, hasItems(AS_IS, NAME, ADDRESS, BIRTHDATE, PASSPORT, PHONE, EMAIL, VEHICLE));
		val records = response.getData();
		assertThat(records, is(notNullValue()));
		CompositeResponse.Record record = records.get(0);
		assertThat(record, is(notNullValue(CompositeResponse.Record.class)));
		assertThat(record.getAsIs(), is(asisMatcher("as is sample 1")));
		assertThat(record.getAddress(), is(SampleAddresses.KHABAROVSK_1.getMatcher()));
		assertThat(record.getBirthDate(), is(SampleBirthDates.VALID_1.getMatcher()));
		assertThat(record.getEmail(), is(SampleEmails.VALID.getMatcher()));
		assertThat(record.getName(), is(SampleNames.MALE_1.getMatcher()));
		assertThat(record.getPassport(), is(SamplePassports.VALID_1.getMatcher()));
		assertThat(record.getPhone(), is(SamplePhones.KHABAROVSK_1.getMatcher()));
		assertThat(record.getVehicle(), is(SampleVehicles.VALID_1.getMatcher()));
		record = records.get(1);
		assertThat(record, is(notNullValue(CompositeResponse.Record.class)));
		assertThat(record.getAsIs(), is(asisMatcher("as is sample 2")));
		assertThat(record.getAddress(), is(SampleAddresses.MOSCOW_1.getMatcher()));
		assertThat(record.getBirthDate(), is(SampleBirthDates.VALID_2.getMatcher()));
		assertThat(record.getEmail(), is(SampleEmails.INSTANT.getMatcher()));
		assertThat(record.getName(), is(SampleNames.FEMALE_1.getMatcher()));
		assertThat(record.getPassport(), is(SamplePassports.VOID_1.getMatcher()));
		assertThat(record.getPhone(), is(SamplePhones.MOSCOW_1.getMatcher()));
		assertThat(record.getVehicle(), is(SampleVehicles.VALID_2.getMatcher()));
		record = records.get(2);
		assertThat(record, is(notNullValue(CompositeResponse.Record.class)));
		Assertions.assertThat(record.get(AS_IS)).isInstanceOfSatisfying(AsIs.class, asis -> assertThat(asis, is(asisMatcher("as is sample 3"))));
		Assertions.assertThat(record.get(ADDRESS)).isInstanceOfSatisfying(Address.class, address -> assertThat(address, is(SampleAddresses.NOVOSIB_1.getMatcher())));
		Assertions.assertThat(record.get(BIRTHDATE)).isInstanceOfSatisfying(BirthDate.class, birthDate -> assertThat(birthDate, is(SampleBirthDates.VALID_3.getMatcher())));
		Assertions.assertThat(record.get(EMAIL)).isInstanceOfSatisfying(Email.class, email -> assertThat(email, is(SampleEmails.CORRECTED.getMatcher())));
		Assertions.assertThat(record.get(NAME)).isInstanceOfSatisfying(Name.class, name -> assertThat(name, is(SampleNames.CHINESE.getMatcher())));
		Assertions.assertThat(record.get(PASSPORT)).isInstanceOfSatisfying(Passport.class, passport -> assertThat(passport, is(SamplePassports.BAD_FORMAT_1.getMatcher())));
		Assertions.assertThat(record.get(PHONE)).isInstanceOfSatisfying(Phone.class, phone -> assertThat(phone, is(SamplePhones.NOVOSIB_MOBILE_1.getMatcher())));
		Assertions.assertThat(record.get(VEHICLE)).isInstanceOfSatisfying(Vehicle.class, vehicle -> assertThat(vehicle, is(SampleVehicles.VALID_3.getMatcher())));
	}

	@Test
	public void compositeResponseGapsOfSamples() throws IOException {
		val response = testCompositeResponse(SampleComposite.SAMPLES_GAPS.getResponseBody());
		assertThat(response, SampleComposite.SAMPLES_GAPS.getResponseMatcher());
	}

	@Test
	public void compositeResponseSplIterator() throws IOException {
		val response = testCompositeResponse(SampleComposite.SAMPLES_FULL.getResponseBody());
		val spliterator = response.spliterator();
		System.out.println("spliterator characteristics: " + spliterator.characteristics());
		System.out.println("spliterator size: " + spliterator.getExactSizeIfKnown());
		StreamSupport.stream(spliterator, true).forEach(r -> System.out.println("record: " + r));
	}

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void structureIsMissing() throws IOException {
		exception.expect(MismatchedInputException.class);
		exception.expectMessage(startsWith("Structure descriptor element is missing, null or empty"));
		testCompositeResponse("{\"notAstructure\":[\"AS_IS\"],\"data\":[[{\"source\":\"as is 1\"}]]}");
	}

	@Test
	public void structureIsNull() throws IOException {
		exception.expect(MismatchedInputException.class);
		exception.expectMessage(startsWith("Structure descriptor element is missing, null or empty"));
		testCompositeResponse("{\"structure\":null,\"data\":[[{\"source\":\"as is 1\"}]]}");
	}

	@Test
	public void structureIsEmpty() throws IOException {
		exception.expect(MismatchedInputException.class);
		exception.expectMessage(startsWith("Structure descriptor element is missing, null or empty"));
		testCompositeResponse("{\"structure\":[],\"data\":[[{\"source\":\"as is 1\"}]]}");
	}

	@Test
	public void structureIsNotArray() throws IOException {
		exception.expect(MismatchedInputException.class);
		exception.expectMessage(startsWith("Structure descriptor element value '\"wrong\"' is not an array"));
		testCompositeResponse("{\"structure\":\"wrong\",\"data\":[[{\"source\":\"as is 1\"}]]}");
	}

	@Test
	public void dataIsMissing() throws IOException {
		exception.expect(MismatchedInputException.class);
		exception.expectMessage(startsWith("Data element is missing"));
		testCompositeResponse("{\"structure\":[\"AS_IS\"],\"notAdata\":[[{\"source\":\"as is 1\"}]]}");
	}

	@Test
	public void dataIsNotArray() throws IOException {
		exception.expect(MismatchedInputException.class);
		exception.expectMessage(startsWith("Data node value '\"wrong\"' is not an array"));
		testCompositeResponse("{\"structure\":[\"AS_IS\"],\"data\":\"wrong\"}");
	}

	@Test
	public void elementIsOutOfStructure() throws IOException {
		exception.expect(MismatchedInputException.class);
		exception.expectMessage(startsWith("Element node #1 of record #0 is out of structure"));
		testCompositeResponse("{\"structure\":[\"AS_IS\"],\"data\":[[{\"source\":\"as is 1\"},{\"source\":\"something\"}]]}");
	}

	@Test
	public void elementIsNotObject() throws IOException {
		exception.expect(MismatchedInputException.class);
		exception.expectMessage(startsWith("Cannot construct instance of `ru.redcom.software.util.integration.api.client.dadata.dto.Phone` (although at least one Creator exists): no String-argument constructor/factory method to deserialize from String value ('wrong')"));
		testCompositeResponse("{\"structure\":[\"AS_IS\",\"PHONE\"],\"data\":[[{\"source\":\"as is 1\"},\"wrong\"]]}");
	}

	@Test
	public void recordNodeIsNull() throws IOException {
		exception.expect(MismatchedInputException.class);
		exception.expectMessage(startsWith("Record #1 node is null"));
		testCompositeResponse("{\"structure\":[\"AS_IS\"],\"data\":[[{\"source\":\"as is 1\"}],null]}");
	}

	@Test
	public void recordNodeIsNotArray() throws IOException {
		exception.expect(MismatchedInputException.class);
		exception.expectMessage(startsWith("Record #1 node value '\"wrong\"' is not an array"));
		testCompositeResponse("{\"structure\":[\"AS_IS\"],\"data\":[[{\"source\":\"as is 1\"}],\"wrong\"]}");
	}

	@Test
	public void parseError1() throws IOException {
		exception.expect(JsonParseException.class);
		exception.expectMessage(startsWith("Unexpected character (']' (code 93)): expected a valid value (number, String, array, object, 'true', 'false' or 'null')"));
		testCompositeResponse("{\"structure\":[\"AS_IS\"],\"data\":[[{\"source\":\"as is 1\"}],]}");
	}

	@Test
	public void parseError2() throws IOException {
		exception.expect(JsonParseException.class);
		exception.expectMessage(startsWith("Unexpected character ('z' (code 122)): was expecting double-quote to start field name"));
		testCompositeResponse("{\"structure\":[\"AS_IS\"],\"data\":[[{z\"source\":\"as is 1\"}]]}");
	}


	private CompositeResponse testCompositeResponse(final String source) throws IOException {
		val response = objectMapper.readValue(source, CompositeResponse.class);
		System.out.println("response: " + response);
		return response;
	}
}
