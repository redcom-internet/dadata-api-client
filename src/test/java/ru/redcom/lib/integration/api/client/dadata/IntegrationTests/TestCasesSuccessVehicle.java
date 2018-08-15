/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.IntegrationTests;

import org.hamcrest.Matcher;
import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.dto.Vehicle;
import ru.redcom.lib.integration.api.client.dadata.types.QcVehicle;

import java.util.function.Function;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TestCasesSuccessVehicle {

	public enum SampleVehicles {
		VALID_1("форд фокус",
		        "[{\"source\":\"форд фокус\",\"result\":\"FORD FOCUS\",\"brand\":\"FORD\",\"model\":\"FOCUS\",\"qc\":0}]",
		        TestCasesSuccessVehicle::matcherValid1),
		VALID_2("Nissan Sunny",
		        "[{\"source\":\"Nissan Sunny\",\"result\":\"NISSAN SUNNY\",\"brand\":\"NISSAN\",\"model\":\"SUNNY\",\"qc\":0}]",
		        TestCasesSuccessVehicle::matcherValid2),
		VALID_3("Лада Гранта",
		        "[{\"source\":\"Лада Гранта\",\"result\":\"ВАЗ GRANTA\",\"brand\":\"ВАЗ\",\"model\":\"GRANTA\",\"qc\":0}]",
		        TestCasesSuccessVehicle::matcherValid3),
		PARTIAL("Лексус",
		        "[{\"source\":\"Лексус\",\"result\":\"LEXUS\",\"brand\":\"LEXUS\",\"model\":null,\"qc\":1}]",
		        TestCasesSuccessVehicle::matcherPartial),
		EMPTY("-",
		      "[{\"source\":\"-\",\"result\":null,\"brand\":null,\"model\":null,\"qc\":2}]",
		      TestCasesSuccessVehicle::matcherEmpty);

		private final String sourcePattern;
		private final String responseBody;
		private final Function<String, Matcher<Vehicle>> matcher;

		SampleVehicles(final String sourcePattern, final String responseBody, final Function<String, Matcher<Vehicle>> matcher) {
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

		public Matcher<Vehicle> getMatcher() {
			return matcher != null ? matcher.apply(sourcePattern) : nullValue(Vehicle.class);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	// форд фокус
	private static Matcher<Vehicle> matcherValid1(final String s) {
		return pojo(Vehicle.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("result", is("FORD FOCUS"))
				.withProperty("brand", is("FORD"))
				.withProperty("model", is("FOCUS"))
				.withProperty("qc", is(QcVehicle.FULL));
	}

	// Nissan Sunny
	private static Matcher<Vehicle> matcherValid2(final String s) {
		return pojo(Vehicle.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("result", is("NISSAN SUNNY"))
				.withProperty("brand", is("NISSAN"))
				.withProperty("model", is("SUNNY"))
				.withProperty("qc", is(QcVehicle.FULL));
	}

	// Лада Гранта
	private static Matcher<Vehicle> matcherValid3(final String s) {
		return pojo(Vehicle.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("result", is("ВАЗ GRANTA"))
				.withProperty("brand", is("ВАЗ"))
				.withProperty("model", is("GRANTA"))
				.withProperty("qc", is(QcVehicle.FULL));
	}

	// Лексус
	private static Matcher<Vehicle> matcherPartial(final String s) {
		return pojo(Vehicle.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("result", is("LEXUS"))
				.withProperty("brand", is("LEXUS"))
				.withProperty("model", is(nullValue(String.class)))
				.withProperty("qc", is(QcVehicle.PARTIAL));
	}

	// empty
	private static Matcher<Vehicle> matcherEmpty(final String s) {
		return pojo(Vehicle.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("result", is(nullValue(String.class)))
				.withProperty("brand", is(nullValue(String.class)))
				.withProperty("model", is(nullValue(String.class)))
				.withProperty("qc", is(QcVehicle.UNRECOGNIZED));
	}

	// =================================================================================================================

	public static void successTest(final DaDataClient dadata, final String sourcePattern, final Matcher<Vehicle> matcher) {
		System.out.println("source Vehicle: " + sourcePattern);
		final Vehicle a = dadata.cleanVehicle(sourcePattern);
		System.out.println("cleaned Vehicle: " + a);
		assertThat(a, is(matcher));
	}
}
