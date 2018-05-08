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
import ru.redcom.software.util.integration.api.client.dadata.dto.Passport;
import ru.redcom.software.util.integration.api.client.dadata.types.QcPassport;

import java.util.function.Function;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.Matchers.*;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessPassport.successTest;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest
public class UT52PassportEnumsMock {
	private static final String URI = "/clean/passport";
	private static final HttpMethod METHOD = HttpMethod.POST;

	@Autowired
	private DaDataClient dadata;
	@Autowired
	private MockRestServiceServer server;

	enum SamplePassport {
		ENUMS_OMITTED("enums omitted",
		              "[{\"source\":\"enums omitted\"}]",
		              UT52PassportEnumsMock::matcherOmitted),
		ENUMS_NULL("enums null",
		           "[{\"source\":\"enums null\",\"qc\":null}]",
		           UT52PassportEnumsMock::matcherNull),
		ENUMS_EMPTY("enums empty",
		            "[{\"source\":\"enums empty\",\"qc\":\"\"}]",
		            UT52PassportEnumsMock::matcherEmpty),
		ENUMS_SET1("enums set 1",
		           "[{\"source\":\"enums set 1\",\"qc\":0}]",
		           UT52PassportEnumsMock::matcherSet1),
		ENUMS_SET2("enums set 2",
		           "[{\"source\":\"enums set 2\",\"qc\":1}]",
		           UT52PassportEnumsMock::matcherSet2),
		ENUMS_SET3("enums set 3",
		           "[{\"source\":\"enums set 3\",\"qc\":2}]",
		           UT52PassportEnumsMock::matcherSet3),
		ENUMS_SET4("enums set 4",
		           "[{\"source\":\"enums set 4\",\"qc\":10}]",
		           UT52PassportEnumsMock::matcherSet4),
		ENUMS_UNKNOWN("enums unknown",
		              "[{\"source\":\"enums unknown\",\"qc\":999}]",
		              UT52PassportEnumsMock::matcherUnknown);

		private final String sourcePattern;
		private final String responseBody;
		private final Function<String, Matcher<Passport>> matcher;

		SamplePassport(final String sourcePattern, final String responseBody, final Function<String, Matcher<Passport>> matcher) {
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

		public Matcher<Passport> getMatcher() {
			return matcher != null ? matcher.apply(sourcePattern) : nullValue(Passport.class);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	// enum fields omitted
	private static Matcher<Passport> matcherOmitted(final String s) {
		return pojo(Passport.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(nullValue(QcPassport.class)));
	}

	// enum fields null
	private static Matcher<Passport> matcherNull(final String s) {
		return pojo(Passport.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(nullValue(QcPassport.class)));
	}

	// enum fields empty
	private static Matcher<Passport> matcherEmpty(final String s) {
		return pojo(Passport.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(nullValue(QcPassport.class)));
	}


	// enum unknown values
	private static Matcher<Passport> matcherUnknown(final String s) {
		return pojo(Passport.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcPassport.UNKNOWN));
	}

	// enum set 1
	private static Matcher<Passport> matcherSet1(final String s) {
		return pojo(Passport.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcPassport.VALID));
	}

	// enum set 2
	private static Matcher<Passport> matcherSet2(final String s) {
		return pojo(Passport.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcPassport.BAD_FORMAT));
	}

	// enum set 3
	private static Matcher<Passport> matcherSet3(final String s) {
		return pojo(Passport.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcPassport.EMPTY));
	}

	// enum set 4
	private static Matcher<Passport> matcherSet4(final String s) {
		return pojo(Passport.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcPassport.VOID));
	}

	// =================================================================================================================

	@Test
	public void enumsOmitted() throws DaDataException {
		test(SamplePassport.ENUMS_OMITTED);
	}

	@Test
	public void enumsNull() throws DaDataException {
		test(SamplePassport.ENUMS_NULL);
	}

	@Test
	public void enumsEmpty() throws DaDataException {
		test(SamplePassport.ENUMS_EMPTY);
	}

	@Test
	public void enumsUnknown() throws DaDataException {
		test(SamplePassport.ENUMS_UNKNOWN);
	}

	@Test
	public void set1() throws DaDataException {
		test(SamplePassport.ENUMS_SET1);
	}

	@Test
	public void set2() throws DaDataException {
		test(SamplePassport.ENUMS_SET2);
	}

	@Test
	public void set3() throws DaDataException {
		test(SamplePassport.ENUMS_SET3);
	}

	@Test
	public void set4() throws DaDataException {
		test(SamplePassport.ENUMS_SET4);
	}

	// -----------------------------------------------------------------------------------------------------------------

	// shared test body
	private void test(final SamplePassport sample) {
		setupTestServer(server, URI, METHOD, sample.getResponseBody());
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
		server.verify();
	}
}
