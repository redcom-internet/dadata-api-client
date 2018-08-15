/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.IntegrationTests;

import org.hamcrest.Matcher;
import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.dto.BirthDate;
import ru.redcom.lib.integration.api.client.dadata.types.QcBirthDate;

import java.time.LocalDate;
import java.util.function.Function;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TestCasesSuccessBirthDate {

	public enum SampleBirthDates {
		VALID_1("01.02.70",
		        "[{\"source\":\"01.02.70\",\"birthdate\":\"01.02.1970\",\"qc\":0}]",
		        TestCasesSuccessBirthDate::matcherValid1),
		VALID_2("02/03/1980",
		        "[{\"source\":\"02/03/1980\",\"birthdate\":\"02.03.1980\",\"qc\":0}]",
		        TestCasesSuccessBirthDate::matcherValid2),
		VALID_3("1990-12-26",
		        "[{\"source\":\"1990-12-26\",\"birthdate\":\"26.12.1990\",\"qc\":0}]",
		        TestCasesSuccessBirthDate::matcherValid3),
		PARTIAL("11.00",
		        "[{\"source\":\"11.00\",\"birthdate\":null,\"qc\":1}]",
		        TestCasesSuccessBirthDate::matcherPartial),
		EMPTY("-",
		      "[{\"source\":\"-\",\"birthdate\":null,\"qc\":2}]",
		      TestCasesSuccessBirthDate::matcherEmpty),
		NULL(null,
		     "{\"source\":null,\"birthdate\":null,\"qc\":2}",
		     s -> matcherNull());

		private final String sourcePattern;
		private final String responseBody;
		private final Function<String, Matcher<BirthDate>> matcher;

		SampleBirthDates(final String sourcePattern, final String responseBody, final Function<String, Matcher<BirthDate>> matcher) {
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

		public Matcher<BirthDate> getMatcher() {
			return matcher != null ? matcher.apply(sourcePattern) : nullValue(BirthDate.class);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	// 01.02.70
	private static Matcher<BirthDate> matcherValid1(final String s) {
		return pojo(BirthDate.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("birthdate", is(equalTo(LocalDate.of(1970, 2, 1))))
				.withProperty("qc", is(QcBirthDate.FULL));
	}

	// 02/03/1980
	private static Matcher<BirthDate> matcherValid2(final String s) {
		return pojo(BirthDate.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("birthdate", is(equalTo(LocalDate.of(1980, 3, 2))))
				.withProperty("qc", is(QcBirthDate.FULL));
	}

	// 1990-12-26
	private static Matcher<BirthDate> matcherValid3(final String s) {
		return pojo(BirthDate.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("birthdate", is(equalTo(LocalDate.of(1990, 12, 26))))
				.withProperty("qc", is(QcBirthDate.FULL));
	}

	// 11.00
	private static Matcher<BirthDate> matcherPartial(final String s) {
		return pojo(BirthDate.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("birthdate", is(nullValue(LocalDate.class)))
				.withProperty("qc", is(QcBirthDate.PARTIAL));
	}

	// empty
	private static Matcher<BirthDate> matcherEmpty(final String s) {
		return pojo(BirthDate.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("birthdate", is(nullValue(LocalDate.class)))
				.withProperty("qc", is(QcBirthDate.UNRECOGNIZED));
	}

	// null
	private static Matcher<BirthDate> matcherNull() {
		return pojo(BirthDate.class)
				.withProperty("source", is(nullValue(LocalDate.class)))
				.withProperty("birthdate", is(nullValue(LocalDate.class)))
				.withProperty("qc", is(QcBirthDate.UNRECOGNIZED));
	}

	// =================================================================================================================

	public static void successTest(final DaDataClient dadata, final String sourcePattern, final Matcher<BirthDate> matcher) {
		System.out.println("source birthdate: " + sourcePattern);
		final BirthDate a = dadata.cleanBirthDate(sourcePattern);
		System.out.println("cleaned birthdate: " + a);
		assertThat(a, is(matcher));
	}
}
