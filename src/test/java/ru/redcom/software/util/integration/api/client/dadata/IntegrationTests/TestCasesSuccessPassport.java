/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests;

import org.hamcrest.Matcher;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClient;
import ru.redcom.software.util.integration.api.client.dadata.dto.Passport;
import ru.redcom.software.util.integration.api.client.dadata.types.QcPassport;

import java.util.function.Function;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TestCasesSuccessPassport {

	public enum SamplePassports {
		VALID_1("4509 235857",
		        "[{\"source\":\"4509 235857\",\"series\":\"45 09\",\"number\":\"235857\",\"qc\":0}]",
		        TestCasesSuccessPassport::matcherValid1),
		VOID_1("0000000000",
		       "[{\"source\":\"0000000000\",\"series\":null,\"number\":null,\"qc\":10}]",
		       TestCasesSuccessPassport::matcherVoid1),
		BAD_FORMAT_1("zzz",
		             "[{\"source\":\"zzz\",\"series\":null,\"number\":null,\"qc\":1}]",
		             TestCasesSuccessPassport::matcherBadFormat1),
		EMPTY_1("-",
		        "[{\"source\":\"-\",\"series\":null,\"number\":null,\"qc\":2}]",
		        TestCasesSuccessPassport::matcherEmpty1);

		private final String sourcePattern;
		private final String responseBody;
		private final Function<String, Matcher<Passport>> matcher;

		SamplePassports(final String sourcePattern, final String responseBody, final Function<String, Matcher<Passport>> matcher) {
			this.sourcePattern = sourcePattern;
			this.responseBody = responseBody;
			this.matcher = matcher;
		}

		public String getSourcePattern() {
			return sourcePattern;
		}

		public String getRequestBody() {
			return "[\"" + sourcePattern + "\"]";
		}

		public String getResponseBody() {
			return responseBody;
		}

		public Matcher<Passport> getMatcher() {
			return matcher != null ? matcher.apply(sourcePattern) : nullValue(Passport.class);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	// 4509 235857
	private static Matcher<Passport> matcherValid1(final String s) {
		return pojo(Passport.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("series", is("45 09"))
				.withProperty("number", is("235857"))
				.withProperty("qc", is(QcPassport.VALID));
	}

	// 0000000000
	private static Matcher<Passport> matcherVoid1(final String s) {
		return pojo(Passport.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("series", is(nullValue(String.class)))
				.withProperty("number", is(nullValue(String.class)))
				.withProperty("qc", is(QcPassport.VOID));
	}

	// zzz
	private static Matcher<Passport> matcherBadFormat1(final String s) {
		return pojo(Passport.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("series", is(nullValue(String.class)))
				.withProperty("number", is(nullValue(String.class)))
				.withProperty("qc", is(QcPassport.BAD_FORMAT));
	}

	// empty
	private static Matcher<Passport> matcherEmpty1(final String s) {
		return pojo(Passport.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("series", is(nullValue(String.class)))
				.withProperty("number", is(nullValue(String.class)))
				.withProperty("qc", is(QcPassport.EMPTY));
	}

	// =================================================================================================================

	public static void successTest(final DaDataClient dadata, final String sourcePattern, final Matcher<Passport> matcher) {
		System.out.println("source phone: " + sourcePattern);
		final Passport a = dadata.cleanPassport(sourcePattern);
		System.out.println("cleaned phone: " + a);
		assertThat(a, is(matcher));
	}
}
