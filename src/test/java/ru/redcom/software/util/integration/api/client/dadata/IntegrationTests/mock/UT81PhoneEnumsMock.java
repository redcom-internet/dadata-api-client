/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClient;
import ru.redcom.software.util.integration.api.client.dadata.DaDataException;
import ru.redcom.software.util.integration.api.client.dadata.dto.Phone;
import ru.redcom.software.util.integration.api.client.dadata.types.QcConflict;
import ru.redcom.software.util.integration.api.client.dadata.types.QcPhone;

import java.util.function.Function;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.Matchers.*;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessPhone.successTest;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest
public class UT81PhoneEnumsMock {
	private static final String URI = "/clean/phone";
	private static final HttpMethod METHOD = HttpMethod.POST;

	@Autowired
	private DaDataClient dadata;
	@Autowired
	private MockRestServiceServer server;

	enum SamplePhone {
		ENUMS_OMITTED("enums omitted",
		              "[{\"source\":\"enums omitted\"}]",
		              UT81PhoneEnumsMock::matcherOmitted),
		ENUMS_NULL("enums null",
		           "[{\"source\":\"enums null\",\"qc_conflict\":null,\"qc\":null}]",
		           UT81PhoneEnumsMock::matcherNull),
		ENUMS_EMPTY("enums empty",
		            "[{\"source\":\"enums empty\",\"qc_conflict\":\"\",\"qc\":\"\"}]",
		            UT81PhoneEnumsMock::matcherEmpty),
		ENUMS_SET1("enums set 1",
		           "[{\"source\":\"enums set 1\",\"qc_conflict\":0,\"qc\":0}]",
		           UT81PhoneEnumsMock::matcherSet1),
		ENUMS_SET2("enums set 2",
		           "[{\"source\":\"enums set 2\",\"qc_conflict\":2,\"qc\":1}]",
		           UT81PhoneEnumsMock::matcherSet2),
		ENUMS_SET3("enums set 3",
		           "[{\"source\":\"enums set 3\",\"qc_conflict\":3,\"qc\":2}]",
		           UT81PhoneEnumsMock::matcherSet3),
		ENUMS_SET4("enums set 4",
		           "[{\"source\":\"enums set 4\",\"qc\":3}]",
		           UT81PhoneEnumsMock::matcherSet4),
		ENUMS_UNKNOWN("enums unknown",
		              "[{\"source\":\"enums unknown\",\"qc_conflict\":999,\"qc\":999}]",
		              UT81PhoneEnumsMock::matcherUnknown);

		private final String sourcePattern;
		private final String responseBody;
		private final Function<String, Matcher<Phone>> matcher;

		SamplePhone(final String sourcePattern, final String responseBody, final Function<String, Matcher<Phone>> matcher) {
			this.sourcePattern = sourcePattern;
			this.responseBody = responseBody;
			this.matcher = matcher;
		}

		public String getSourcePattern() {
			return sourcePattern;
		}

		public String getResponseBody() {
			return responseBody;
		}

		public Matcher<Phone> getMatcher() {
			return matcher != null ? matcher.apply(sourcePattern) : nullValue(Phone.class);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	// enum fields omitted
	private static Matcher<Phone> matcherOmitted(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qcConflict", is(nullValue(QcConflict.class)))
				.withProperty("qc", is(nullValue(QcPhone.class)));
	}

	// enum fields null
	private static Matcher<Phone> matcherNull(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qcConflict", is(nullValue(QcConflict.class)))
				.withProperty("qc", is(nullValue(QcPhone.class)));
	}

	// enum fields empty
	private static Matcher<Phone> matcherEmpty(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qcConflict", is(nullValue(QcConflict.class)))
				.withProperty("qc", is(nullValue(QcPhone.class)));
	}


	// enum unknown values
	private static Matcher<Phone> matcherUnknown(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qcConflict", is(QcConflict.UNKNOWN))
				.withProperty("qc", is(QcPhone.UNKNOWN));
	}

	// enum set 1
	private static Matcher<Phone> matcherSet1(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// enum set 2
	private static Matcher<Phone> matcherSet2(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qcConflict", is(QcConflict.CITY_DIFFERS))
				.withProperty("qc", is(QcPhone.PARTIAL));
	}

	// enum set 3
	private static Matcher<Phone> matcherSet3(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qcConflict", is(QcConflict.REGION_DIFFERS))
				.withProperty("qc", is(QcPhone.UNRECOGNIZED));
	}

	// enum set 4
	private static Matcher<Phone> matcherSet4(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcPhone.INVARIANTS));
	}

	// =================================================================================================================

	@Test
	public void enumsOmitted() throws DaDataException {
		test(SamplePhone.ENUMS_OMITTED);
	}

	@Test
	public void enumsNull() throws DaDataException {
		test(SamplePhone.ENUMS_NULL);
	}

	@Test
	public void enumsEmpty() throws DaDataException {
		test(SamplePhone.ENUMS_EMPTY);
	}

	@Test
	public void enumsUnknown() throws DaDataException {
		test(SamplePhone.ENUMS_UNKNOWN);
	}

	@Test
	public void set1() throws DaDataException {
		test(SamplePhone.ENUMS_SET1);
	}

	@Test
	public void set2() throws DaDataException {
		test(SamplePhone.ENUMS_SET2);
	}

	@Test
	public void set3() throws DaDataException {
		test(SamplePhone.ENUMS_SET3);
	}

	@Test
	public void set4() throws DaDataException {
		test(SamplePhone.ENUMS_SET4);
	}

	// -----------------------------------------------------------------------------------------------------------------

	// shared test body
	private void test(final SamplePhone sample) {
		setupTestServer(server, URI, METHOD, sample.getResponseBody());
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
		server.verify();
	}
}
