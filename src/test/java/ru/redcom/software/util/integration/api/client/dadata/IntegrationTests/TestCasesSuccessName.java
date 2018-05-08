/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests;

import org.hamcrest.Matcher;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClient;
import ru.redcom.software.util.integration.api.client.dadata.dto.Name;
import ru.redcom.software.util.integration.api.client.dadata.types.Gender;
import ru.redcom.software.util.integration.api.client.dadata.types.QcName;

import java.util.function.Function;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TestCasesSuccessName {

	public enum SampleNames {
		MALE_1("Иванов Пётр Васильевич",
		       "[{\"source\":\"Иванов Пётр Васильевич\",\"result\":\"Иванов Пётр Васильевич\",\"result_genitive\":\"Иванова Пётра Васильевича\",\"result_dative\":\"Иванову Пётру Васильевичу\",\"result_ablative\":\"Ивановым Пётром Васильевичем\",\"surname\":\"Иванов\",\"name\":\"Пётр\",\"patronymic\":\"Васильевич\",\"gender\":\"М\",\"qc\":0}]",
		       TestCasesSuccessName::matcherMale1),
		FEMALE_1("Петрова Татьяна Денисовна",
		         "[{\"source\":\"Петрова Татьяна Денисовна\",\"result\":\"Петрова Татьяна Денисовна\",\"result_genitive\":\"Петровой Татьяны Денисовны\",\"result_dative\":\"Петровой Татьяне Денисовне\",\"result_ablative\":\"Петровой Татьяной Денисовной\",\"surname\":\"Петрова\",\"name\":\"Татьяна\",\"patronymic\":\"Денисовна\",\"gender\":\"Ж\",\"qc\":0}]",
		         TestCasesSuccessName::matcherFemale1),
		CHINESE("Си Хуан Ли",
		        "[{\"source\":\"Си Хуан Ли\",\"result\":\"Си Хуан Ли\",\"result_genitive\":null,\"result_dative\":null,\"result_ablative\":null,\"surname\":\"Си\",\"name\":\"Хуан Ли\",\"patronymic\":null,\"gender\":\"М\",\"qc\":0}]",
		        TestCasesSuccessName::matcherChinese),
		SUSPICIOUS("Ы Голоньяк",
		           "[{\"source\":\"Ы Голоньяк\",\"result\":\"Голоньяк\",\"result_genitive\":null,\"result_dative\":null,\"result_ablative\":null,\"surname\":null,\"name\":\"Голоньяк\",\"patronymic\":null,\"gender\":\"НД\",\"qc\":1}]",
		           TestCasesSuccessName::matcherSuspicious),
		EMPTY("-",
		      "[{\"source\":\"-\",\"result\":null,\"result_genitive\":null,\"result_dative\":null,\"result_ablative\":null,\"surname\":null,\"name\":null,\"patronymic\":null,\"gender\":\"НД\",\"qc\":1}]",
		      TestCasesSuccessName::matcherEmpty);

		private final String sourcePattern;
		private final String responseBody;
		private final Function<String, Matcher<Name>> matcher;

		SampleNames(final String sourcePattern, final String responseBody, final Function<String, Matcher<Name>> matcher) {
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

		public Matcher<Name> getMatcher() {
			return matcher != null ? matcher.apply(sourcePattern) : nullValue(Name.class);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	// Иванов Пётр Васильевич
	private static Matcher<Name> matcherMale1(final String s) {
		return pojo(Name.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("result", is(equalTo(s)))
				.withProperty("resultGenitive", is("Иванова Пётра Васильевича"))
				.withProperty("resultDative", is("Иванову Пётру Васильевичу"))
				.withProperty("resultAblative", is("Ивановым Пётром Васильевичем"))
				.withProperty("surname", is("Иванов"))
				.withProperty("name", is("Пётр"))
				.withProperty("patronymic", is("Васильевич"))
				.withProperty("gender", is(Gender.MALE))
				.withProperty("qc", is(QcName.FULL));
	}

	// Петрова Татьяна Денисовна
	private static Matcher<Name> matcherFemale1(final String s) {
		return pojo(Name.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("result", is(equalTo(s)))
				.withProperty("resultGenitive", is("Петровой Татьяны Денисовны"))
				.withProperty("resultDative", is("Петровой Татьяне Денисовне"))
				.withProperty("resultAblative", is("Петровой Татьяной Денисовной"))
				.withProperty("surname", is("Петрова"))
				.withProperty("name", is("Татьяна"))
				.withProperty("patronymic", is("Денисовна"))
				.withProperty("gender", is(Gender.FEMALE))
				.withProperty("qc", is(QcName.FULL));
	}

	// Си Хуан Ли
	private static Matcher<Name> matcherChinese(final String s) {
		return pojo(Name.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("result", is(equalTo(s)))
				.withProperty("resultGenitive", is(nullValue(String.class)))
				.withProperty("resultDative", is(nullValue(String.class)))
				.withProperty("resultAblative", is(nullValue(String.class)))
				.withProperty("surname", is("Си"))
				.withProperty("name", is("Хуан Ли"))
				.withProperty("patronymic", is(nullValue(String.class)))
				.withProperty("gender", is(Gender.MALE))
				.withProperty("qc", is(QcName.FULL));
	}

	// Ы Голоньяк
	private static Matcher<Name> matcherSuspicious(final String s) {
		return pojo(Name.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("result", is("Голоньяк"))
				.withProperty("resultGenitive", is(nullValue(String.class)))
				.withProperty("resultDative", is(nullValue(String.class)))
				.withProperty("resultAblative", is(nullValue(String.class)))
				.withProperty("surname", is(nullValue(String.class)))
				.withProperty("name", is("Голоньяк"))
				.withProperty("patronymic", is(nullValue(String.class)))
				.withProperty("gender", is(Gender.UNKNOWN))
				.withProperty("qc", is(QcName.PARTIAL));
	}

	// empty
	private static Matcher<Name> matcherEmpty(final String s) {
		return pojo(Name.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("result", is(nullValue(String.class)))
				.withProperty("resultGenitive", is(nullValue(String.class)))
				.withProperty("resultDative", is(nullValue(String.class)))
				.withProperty("resultAblative", is(nullValue(String.class)))
				.withProperty("surname", is(nullValue(String.class)))
				.withProperty("name", is(nullValue(String.class)))
				.withProperty("patronymic", is(nullValue(String.class)))
				.withProperty("gender", is(Gender.UNKNOWN))
				.withProperty("qc", is(QcName.PARTIAL));
	}

	// =================================================================================================================

	public static void successTest(final DaDataClient dadata, final String sourcePattern, final Matcher<Name> matcher) {
		System.out.println("source name: " + sourcePattern);
		final Name a = dadata.cleanName(sourcePattern);
		System.out.println("cleaned name: " + a);
		assertThat(a, is(matcher));
	}
}
