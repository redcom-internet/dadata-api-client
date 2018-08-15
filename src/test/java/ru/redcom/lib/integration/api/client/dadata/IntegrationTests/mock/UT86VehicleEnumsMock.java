/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.IntegrationTests.mock;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.DaDataException;
import ru.redcom.lib.integration.api.client.dadata.dto.Vehicle;
import ru.redcom.lib.integration.api.client.dadata.types.QcVehicle;

import java.util.function.Function;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.Matchers.*;
import static ru.redcom.lib.integration.api.client.dadata.IntegrationTests.TestCasesSuccessVehicle.successTest;
import static ru.redcom.lib.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest
public class UT86VehicleEnumsMock {
	private static final String URI = "/clean/vehicle";
	private static final HttpMethod METHOD = HttpMethod.POST;

	@Autowired
	private DaDataClient dadata;
	@Autowired
	private MockRestServiceServer server;

	enum SampleVehicle {
		ENUMS_OMITTED("enums omitted",
		              "[{\"source\":\"enums omitted\"}]",
		              UT86VehicleEnumsMock::matcherOmitted),
		ENUMS_NULL("enums null",
		           "[{\"source\":\"enums null\",\"qc\":null}]",
		           UT86VehicleEnumsMock::matcherNull),
		ENUMS_EMPTY("enums empty",
		            "[{\"source\":\"enums empty\",\"qc\":\"\"}]",
		            UT86VehicleEnumsMock::matcherEmpty),
		ENUMS_SET1("enums set 1",
		           "[{\"source\":\"enums set 1\",\"qc\":0}]",
		           UT86VehicleEnumsMock::matcherSet1),
		ENUMS_SET2("enums set 2",
		           "[{\"source\":\"enums set 2\",\"qc\":1}]",
		           UT86VehicleEnumsMock::matcherSet2),
		ENUMS_SET3("enums set 3",
		           "[{\"source\":\"enums set 3\",\"qc\":2}]",
		           UT86VehicleEnumsMock::matcherSet3),
		ENUMS_UNKNOWN("enums unknown",
		              "[{\"source\":\"enums unknown\",\"qc\":999}]",
		              UT86VehicleEnumsMock::matcherUnknown);

		private final String sourcePattern;
		private final String responseBody;
		private final Function<String, Matcher<Vehicle>> matcher;

		SampleVehicle(final String sourcePattern, final String responseBody, final Function<String, Matcher<Vehicle>> matcher) {
			this.sourcePattern = sourcePattern;
			this.responseBody = responseBody;
			this.matcher = matcher;
		}

		String getSourcePattern() {
			return sourcePattern;
		}

		String getResponseBody() {
			return responseBody;
		}

		Matcher<Vehicle> getMatcher() {
			return matcher != null ? matcher.apply(sourcePattern) : nullValue(Vehicle.class);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	// enum fields omitted
	private static Matcher<Vehicle> matcherOmitted(final String s) {
		return pojo(Vehicle.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(nullValue(QcVehicle.class)));
	}

	// enum fields null
	private static Matcher<Vehicle> matcherNull(final String s) {
		return pojo(Vehicle.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(nullValue(QcVehicle.class)));
	}

	// enum fields empty
	private static Matcher<Vehicle> matcherEmpty(final String s) {
		return pojo(Vehicle.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(nullValue(QcVehicle.class)));
	}


	// enum unknown values
	private static Matcher<Vehicle> matcherUnknown(final String s) {
		return pojo(Vehicle.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcVehicle.UNKNOWN));
	}

	// enum set 1
	private static Matcher<Vehicle> matcherSet1(final String s) {
		return pojo(Vehicle.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcVehicle.FULL));
	}

	// enum set 2
	private static Matcher<Vehicle> matcherSet2(final String s) {
		return pojo(Vehicle.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcVehicle.PARTIAL));
	}

	// enum set 3
	private static Matcher<Vehicle> matcherSet3(final String s) {
		return pojo(Vehicle.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcVehicle.UNRECOGNIZED));
	}

	// =================================================================================================================

	@Test
	public void enumsOmitted() throws DaDataException {
		test(SampleVehicle.ENUMS_OMITTED);
	}

	@Test
	public void enumsNull() throws DaDataException {
		test(SampleVehicle.ENUMS_NULL);
	}

	@Test
	public void enumsEmpty() throws DaDataException {
		test(SampleVehicle.ENUMS_EMPTY);
	}

	@Test
	public void enumsUnknown() throws DaDataException {
		test(SampleVehicle.ENUMS_UNKNOWN);
	}

	@Test
	public void set1() throws DaDataException {
		test(SampleVehicle.ENUMS_SET1);
	}

	@Test
	public void set2() throws DaDataException {
		test(SampleVehicle.ENUMS_SET2);
	}

	@Test
	public void set3() throws DaDataException {
		test(SampleVehicle.ENUMS_SET3);
	}

	// -----------------------------------------------------------------------------------------------------------------

	// shared test body
	private void test(final SampleVehicle sample) {
		setupTestServer(server, URI, METHOD, sample.getResponseBody());
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
		server.verify();
	}
}
