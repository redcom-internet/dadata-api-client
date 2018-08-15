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
import ru.redcom.lib.integration.api.client.dadata.dto.Name;
import ru.redcom.lib.integration.api.client.dadata.types.Gender;
import ru.redcom.lib.integration.api.client.dadata.types.QcName;

import java.util.function.Function;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.Matchers.*;
import static ru.redcom.lib.integration.api.client.dadata.IntegrationTests.TestCasesSuccessName.successTest;
import static ru.redcom.lib.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest
public class UT83NameEnumsMock {
	private static final String URI = "/clean/name";
	private static final HttpMethod METHOD = HttpMethod.POST;

	@Autowired
	private DaDataClient dadata;
	@Autowired
	private MockRestServiceServer server;

	enum SampleName {
		ENUMS_OMITTED("enums omitted",
		              "[{\"source\":\"enums omitted\"}]",
		              UT83NameEnumsMock::matcherOmitted),
		ENUMS_NULL("enums null",
		           "[{\"source\":\"enums null\",\"gender\":null,\"qc\":null}]",
		           UT83NameEnumsMock::matcherNull),
		ENUMS_EMPTY("enums empty",
		            "[{\"source\":\"enums empty\",\"gender\":\"\",\"qc\":\"\"}]",
		            UT83NameEnumsMock::matcherEmpty),
		ENUMS_SET1("enums set 1",
		           "[{\"source\":\"enums set 1\",\"gender\":\"М\",\"qc\":0}]",
		           UT83NameEnumsMock::matcherSet1),
		ENUMS_SET2("enums set 2",
		           "[{\"source\":\"enums set 2\",\"gender\":\"Ж\",\"qc\":1}]",
		           UT83NameEnumsMock::matcherSet2),
		ENUMS_SET3("enums set 3",
		           "[{\"source\":\"enums set 3\",\"gender\":\"НД\",\"qc\":2}]",
		           UT83NameEnumsMock::matcherSet3),
		ENUMS_UNKNOWN("enums unknown",
		              "[{\"source\":\"enums unknown\",\"gender\":\"-\",\"qc\":999}]",
		              UT83NameEnumsMock::matcherUnknown);

		private final String sourcePattern;
		private final String responseBody;
		private final Function<String, Matcher<Name>> matcher;

		SampleName(final String sourcePattern, final String responseBody, final Function<String, Matcher<Name>> matcher) {
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

		Matcher<Name> getMatcher() {
			return matcher != null ? matcher.apply(sourcePattern) : nullValue(Name.class);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	// enum fields omitted
	private static Matcher<Name> matcherOmitted(final String s) {
		return pojo(Name.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("gender", is(nullValue(Gender.class)))
				.withProperty("qc", is(nullValue(QcName.class)));
	}

	// enum fields null
	private static Matcher<Name> matcherNull(final String s) {
		return pojo(Name.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("gender", is(nullValue(Gender.class)))
				.withProperty("qc", is(nullValue(QcName.class)));
	}

	// enum fields empty
	private static Matcher<Name> matcherEmpty(final String s) {
		return pojo(Name.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("gender", is(Gender.UNKNOWN))
				.withProperty("qc", is(nullValue(QcName.class)));
	}


	// enum unknown values
	private static Matcher<Name> matcherUnknown(final String s) {
		return pojo(Name.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("gender", is(Gender.UNKNOWN))
				.withProperty("qc", is(QcName.UNKNOWN));
	}

	// enum set 1
	private static Matcher<Name> matcherSet1(final String s) {
		return pojo(Name.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("gender", is(Gender.MALE))
				.withProperty("qc", is(QcName.FULL));
	}

	// enum set 2
	private static Matcher<Name> matcherSet2(final String s) {
		return pojo(Name.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("gender", is(Gender.FEMALE))
				.withProperty("qc", is(QcName.PARTIAL));
	}

	// enum set 3
	private static Matcher<Name> matcherSet3(final String s) {
		return pojo(Name.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("gender", is(Gender.UNKNOWN))
				.withProperty("qc", is(QcName.UNRECOGNIZED));
	}

	// =================================================================================================================

	@Test
	public void enumsOmitted() throws DaDataException {
		test(SampleName.ENUMS_OMITTED);
	}

	@Test
	public void enumsNull() throws DaDataException {
		test(SampleName.ENUMS_NULL);
	}

	@Test
	public void enumsEmpty() throws DaDataException {
		test(SampleName.ENUMS_EMPTY);
	}

	@Test
	public void enumsUnknown() throws DaDataException {
		test(SampleName.ENUMS_UNKNOWN);
	}

	@Test
	public void set1() throws DaDataException {
		test(SampleName.ENUMS_SET1);
	}

	@Test
	public void set2() throws DaDataException {
		test(SampleName.ENUMS_SET2);
	}

	@Test
	public void set3() throws DaDataException {
		test(SampleName.ENUMS_SET3);
	}

	// -----------------------------------------------------------------------------------------------------------------

	// shared test body
	private void test(final SampleName sample) {
		setupTestServer(server, URI, METHOD, sample.getResponseBody());
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
		server.verify();
	}
}
