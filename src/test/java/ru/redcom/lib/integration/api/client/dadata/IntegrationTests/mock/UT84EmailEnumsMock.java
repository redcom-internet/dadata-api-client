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
import ru.redcom.lib.integration.api.client.dadata.dto.Email;
import ru.redcom.lib.integration.api.client.dadata.types.QcEmail;

import java.util.function.Function;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.Matchers.*;
import static ru.redcom.lib.integration.api.client.dadata.IntegrationTests.TestCasesSuccessEmail.successTest;
import static ru.redcom.lib.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest
public class UT84EmailEnumsMock {
	private static final String URI = "/clean/email";
	private static final HttpMethod METHOD = HttpMethod.POST;

	@Autowired
	private DaDataClient dadata;
	@Autowired
	private MockRestServiceServer server;

	enum SampleEmail {
		ENUMS_OMITTED("enums omitted",
		              "[{\"source\":\"enums omitted\"}]",
		              UT84EmailEnumsMock::matcherOmitted),
		ENUMS_NULL("enums null",
		           "[{\"source\":\"enums null\",\"qc\":null}]",
		           UT84EmailEnumsMock::matcherNull),
		ENUMS_EMPTY("enums empty",
		            "[{\"source\":\"enums empty\",\"qc\":\"\"}]",
		            UT84EmailEnumsMock::matcherEmpty),
		ENUMS_SET1("enums set 1",
		           "[{\"source\":\"enums set 1\",\"qc\":0}]",
		           UT84EmailEnumsMock::matcherSet1),
		ENUMS_SET2("enums set 2",
		           "[{\"source\":\"enums set 2\",\"qc\":1}]",
		           UT84EmailEnumsMock::matcherSet2),
		ENUMS_SET3("enums set 3",
		           "[{\"source\":\"enums set 3\",\"qc\":2}]",
		           UT84EmailEnumsMock::matcherSet3),
		ENUMS_SET4("enums set 4",
		           "[{\"source\":\"enums set 4\",\"qc\":3}]",
		           UT84EmailEnumsMock::matcherSet4),
		ENUMS_SET5("enums set 5",
		           "[{\"source\":\"enums set 5\",\"qc\":4}]",
		           UT84EmailEnumsMock::matcherSet5),
		ENUMS_UNKNOWN("enums unknown",
		              "[{\"source\":\"enums unknown\",\"qc\":999}]",
		              UT84EmailEnumsMock::matcherUnknown);

		private final String sourcePattern;
		private final String responseBody;
		private final Function<String, Matcher<Email>> matcher;

		SampleEmail(final String sourcePattern, final String responseBody, final Function<String, Matcher<Email>> matcher) {
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

		Matcher<Email> getMatcher() {
			return matcher != null ? matcher.apply(sourcePattern) : nullValue(Email.class);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	// enum fields omitted
	private static Matcher<Email> matcherOmitted(final String s) {
		return pojo(Email.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(nullValue(QcEmail.class)));
	}

	// enum fields null
	private static Matcher<Email> matcherNull(final String s) {
		return pojo(Email.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(nullValue(QcEmail.class)));
	}

	// enum fields empty
	private static Matcher<Email> matcherEmpty(final String s) {
		return pojo(Email.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(nullValue(Email.class)));
	}


	// enum unknown values
	private static Matcher<Email> matcherUnknown(final String s) {
		return pojo(Email.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcEmail.UNKNOWN));
	}

	// enum set 1
	private static Matcher<Email> matcherSet1(final String s) {
		return pojo(Email.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcEmail.VALID));
	}

	// enum set 2
	private static Matcher<Email> matcherSet2(final String s) {
		return pojo(Email.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcEmail.INVALID));
	}

	// enum set 3
	private static Matcher<Email> matcherSet3(final String s) {
		return pojo(Email.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcEmail.UNRECOGNIZED));
	}

	// enum set 4
	private static Matcher<Email> matcherSet4(final String s) {
		return pojo(Email.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcEmail.INSTANT));
	}

	// enum set 5
	private static Matcher<Email> matcherSet5(final String s) {
		return pojo(Email.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("qc", is(QcEmail.CORRECTED));
	}

	// =================================================================================================================

	@Test
	public void enumsOmitted() throws DaDataException {
		test(SampleEmail.ENUMS_OMITTED);
	}

	@Test
	public void enumsNull() throws DaDataException {
		test(SampleEmail.ENUMS_NULL);
	}

	@Test
	public void enumsEmpty() throws DaDataException {
		test(SampleEmail.ENUMS_EMPTY);
	}

	@Test
	public void enumsUnknown() throws DaDataException {
		test(SampleEmail.ENUMS_UNKNOWN);
	}

	@Test
	public void set1() throws DaDataException {
		test(SampleEmail.ENUMS_SET1);
	}

	@Test
	public void set2() throws DaDataException {
		test(SampleEmail.ENUMS_SET2);
	}

	@Test
	public void set3() throws DaDataException {
		test(SampleEmail.ENUMS_SET3);
	}

	@Test
	public void set4() throws DaDataException {
		test(SampleEmail.ENUMS_SET4);
	}

	@Test
	public void set5() throws DaDataException {
		test(SampleEmail.ENUMS_SET5);
	}

	// -----------------------------------------------------------------------------------------------------------------

	// shared test body
	private void test(final SampleEmail sample) {
		setupTestServer(server, URI, METHOD, sample.getResponseBody());
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
		server.verify();
	}
}
