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
import ru.redcom.lib.integration.api.client.dadata.dto.BirthDate;
import ru.redcom.lib.integration.api.client.dadata.types.QcBirthDate;

import java.util.function.Function;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.Matchers.*;
import static ru.redcom.lib.integration.api.client.dadata.IntegrationTests.TestCasesSuccessBirthDate.successTest;
import static ru.redcom.lib.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest
public class UT85BirthDateEnumsMock {
	private static final String URI = "/clean/birthdate";
	private static final HttpMethod METHOD = HttpMethod.POST;

	@Autowired
	private DaDataClient dadata;
	@Autowired
	private MockRestServiceServer server;

	enum SampleBirthDate {
		ENUMS_OMITTED("enums omitted",
		              "[{\"source\":\"enums omitted\"}]",
		              UT85BirthDateEnumsMock::matcherOmitted),
		ENUMS_NULL("enums null",
		           "[{\"source\":\"enums null\",\"qc\":null}]",
		           UT85BirthDateEnumsMock::matcherNull),
		ENUMS_EMPTY("enums empty",
		            "[{\"source\":\"enums empty\",\"qc\":\"\"}]",
		            UT85BirthDateEnumsMock::matcherEmpty),
		ENUMS_SET1("enums set 1",
		           "[{\"source\":\"enums set 1\",\"qc\":0}]",
		           UT85BirthDateEnumsMock::matcherSet1),
		ENUMS_SET2("enums set 2",
		           "[{\"source\":\"enums set 2\",\"qc\":1}]",
		           UT85BirthDateEnumsMock::matcherSet2),
		ENUMS_SET3("enums set 3",
		           "[{\"source\":\"enums set 3\",\"qc\":2}]",
		           UT85BirthDateEnumsMock::matcherSet3),
		ENUMS_UNKNOWN("enums unknown",
		              "[{\"source\":\"enums unknown\",\"qc\":999}]",
		              UT85BirthDateEnumsMock::matcherUnknown);

		private final String sourcePattern;
		private final String responseBody;
		private final Function<String, Matcher<BirthDate>> matcher;

		SampleBirthDate(final String sourcePattern, final String responseBody, final Function<String, Matcher<BirthDate>> matcher) {
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

		Matcher<BirthDate> getMatcher() {
			return matcher != null ? matcher.apply(sourcePattern) : nullValue(BirthDate.class);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	// enum fields omitted
	private static Matcher<BirthDate> matcherOmitted(final String s) {
		return pojo(BirthDate.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(nullValue(QcBirthDate.class)));
	}

	// enum fields null
	private static Matcher<BirthDate> matcherNull(final String s) {
		return pojo(BirthDate.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(nullValue(QcBirthDate.class)));
	}

	// enum fields empty
	private static Matcher<BirthDate> matcherEmpty(final String s) {
		return pojo(BirthDate.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(nullValue(QcBirthDate.class)));
	}


	// enum unknown values
	private static Matcher<BirthDate> matcherUnknown(final String s) {
		return pojo(BirthDate.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcBirthDate.UNKNOWN));
	}

	// enum set 1
	private static Matcher<BirthDate> matcherSet1(final String s) {
		return pojo(BirthDate.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcBirthDate.FULL));
	}

	// enum set 2
	private static Matcher<BirthDate> matcherSet2(final String s) {
		return pojo(BirthDate.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcBirthDate.PARTIAL));
	}

	// enum set 3
	private static Matcher<BirthDate> matcherSet3(final String s) {
		return pojo(BirthDate.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcBirthDate.UNRECOGNIZED));
	}

	// =================================================================================================================

	@Test
	public void enumsOmitted() throws DaDataException {
		test(SampleBirthDate.ENUMS_OMITTED);
	}

	@Test
	public void enumsNull() throws DaDataException {
		test(SampleBirthDate.ENUMS_NULL);
	}

	@Test
	public void enumsEmpty() throws DaDataException {
		test(SampleBirthDate.ENUMS_EMPTY);
	}

	@Test
	public void enumsUnknown() throws DaDataException {
		test(SampleBirthDate.ENUMS_UNKNOWN);
	}

	@Test
	public void set1() throws DaDataException {
		test(SampleBirthDate.ENUMS_SET1);
	}

	@Test
	public void set2() throws DaDataException {
		test(SampleBirthDate.ENUMS_SET2);
	}

	@Test
	public void set3() throws DaDataException {
		test(SampleBirthDate.ENUMS_SET3);
	}

	// -----------------------------------------------------------------------------------------------------------------

	// shared test body
	private void test(final SampleBirthDate sample) {
		setupTestServer(server, URI, METHOD, sample.getResponseBody());
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
		server.verify();
	}
}
