/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests;

import org.hamcrest.Matcher;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClient;
import ru.redcom.software.util.integration.api.client.dadata.dto.Email;
import ru.redcom.software.util.integration.api.client.dadata.types.QcEmail;

import java.util.function.Function;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TestCasesSuccessEmail {

	public enum SampleEmails {
		VALID("vasya@mail.ru",
		      "[{\"source\":\"vasya@mail.ru\",\"email\":\"vasya@mail.ru\",\"qc\":0}]",
		      TestCasesSuccessEmail::matcherValid),
		INVALID("alice@",
		        "[{\"source\":\"alice@\",\"email\":null,\"qc\":1}]",
		        TestCasesSuccessEmail::matcherInvalid),
		INSTANT("petya@temp-mail.ru",
		        "[{\"source\":\"petya@temp-mail.ru\",\"email\":\"petya@temp-mail.ru\",\"qc\":3}]",
		        TestCasesSuccessEmail::matcherInstant),
		CORRECTED("bob@yandex,ru",
		          "[{\"source\":\"bob@yandex,ru\",\"email\":\"bob@yandex.ru\",\"qc\":1}]",
		          TestCasesSuccessEmail::matcherCorrected),
		UNRECOGNIZED("!%^&#",
		             "[{\"source\":\"!%^&#\",\"email\":null,\"qc\":2}]",
		             TestCasesSuccessEmail::matcherUnrecognized),
		EMPTY("-",
		      "[{\"source\":\"-\",\"email\":null,\"qc\":2}]",
		      TestCasesSuccessEmail::matcherUnrecognized);

		private final String sourcePattern;
		private final String responseBody;
		private final Function<String, Matcher<Email>> matcher;

		SampleEmails(final String sourcePattern, final String responseBody, final Function<String, Matcher<Email>> matcher) {
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

		public Matcher<Email> getMatcher() {
			return matcher != null ? matcher.apply(sourcePattern) : nullValue(Email.class);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	// vasya@mail.ru
	private static Matcher<Email> matcherValid(final String s) {
		return pojo(Email.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("email", is(equalTo(s)))
				.withProperty("qc", is(QcEmail.VALID));
	}

	// alice@
	private static Matcher<Email> matcherInvalid(final String s) {
		return pojo(Email.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("email", is(nullValue(String.class)))
				.withProperty("qc", is(QcEmail.INVALID));
	}

	// petya@temp-mail.ru
	private static Matcher<Email> matcherInstant(final String s) {
		return pojo(Email.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("email", is(equalTo(s)))
				.withProperty("qc", is(QcEmail.INSTANT));
	}

	// bob@yandex,ru
	private static Matcher<Email> matcherCorrected(final String s) {
		return pojo(Email.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("email", is("bob@yandex.ru"))
				.withProperty("qc", is(QcEmail.INVALID));
	}

	// !%^&#
	// empty
	private static Matcher<Email> matcherUnrecognized(final String s) {
		return pojo(Email.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("email", is(nullValue(String.class)))
				.withProperty("qc", is(QcEmail.UNRECOGNIZED));
	}

	// =================================================================================================================

	public static void successTest(final DaDataClient dadata, final String sourcePattern, final Matcher<Email> matcher) {
		System.out.println("source email: " + sourcePattern);
		final Email a = dadata.cleanEmail(sourcePattern);
		System.out.println("cleaned email: " + a);
		assertThat(a, is(matcher));
	}
}
