/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.IntegrationTests;

import org.hamcrest.Matcher;
import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.dto.Phone;
import ru.redcom.lib.integration.api.client.dadata.types.QcConflict;
import ru.redcom.lib.integration.api.client.dadata.types.QcPhone;

import java.util.function.Function;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TestCasesSuccessPhone {

	public enum SamplePhones {
		KHABAROVSK_1("4212450045",
		             "[{\"source\":\"4212450045\",\"type\":\"Стационарный\",\"phone\":\"+7 4212 45-00-45\",\"country_code\":\"7\",\"city_code\":\"4212\",\"number\":\"450045\",\"extension\":null,\"provider\":\"АО \\\"Рэдком-Интернет\\\"\",\"region\":\"Хабаровский\",\"timezone\":\"UTC+10\",\"qc_conflict\":0,\"qc\":0}]",
		             TestCasesSuccessPhone::matcherKhabarovsk1),
		KHABAROVSK_2("84212450045",
		             "[{\"source\":\"84212450045\",\"type\":\"Стационарный\",\"phone\":\"+7 4212 45-00-45\",\"country_code\":\"7\",\"city_code\":\"4212\",\"number\":\"450045\",\"extension\":null,\"provider\":\"АО \\\"Рэдком-Интернет\\\"\",\"region\":\"Хабаровский\",\"timezone\":\"UTC+10\",\"qc_conflict\":0,\"qc\":0}]",
		             TestCasesSuccessPhone::matcherKhabarovsk1),
		KHABAROVSK_3("+74212450045",
		             "[{\"source\":\"+74212450045\",\"type\":\"Стационарный\",\"phone\":\"+7 4212 45-00-45\",\"country_code\":\"7\",\"city_code\":\"4212\",\"number\":\"450045\",\"extension\":null,\"provider\":\"АО \\\"Рэдком-Интернет\\\"\",\"region\":\"Хабаровский\",\"timezone\":\"UTC+10\",\"qc_conflict\":0,\"qc\":0}]",
		             TestCasesSuccessPhone::matcherKhabarovsk1),
		KHABAROVSK_4("8 (4212) 450045 доб.300",
		             "[{\"source\":\"8 (4212) 450045 доб.300\",\"type\":\"Стационарный\",\"phone\":\"+7 4212 45-00-45 доб. 300\",\"country_code\":\"7\",\"city_code\":\"4212\",\"number\":\"450045\",\"extension\":\"300\",\"provider\":\"АО \\\"Рэдком-Интернет\\\"\",\"region\":\"Хабаровский\",\"timezone\":\"UTC+10\",\"qc_conflict\":0,\"qc\":0}]",
		             TestCasesSuccessPhone::matcherKhabarovsk4),
		KHABAROVSK_5("4212214512",
		             "[{\"source\":\"4212214512\",\"type\":\"Стационарный\",\"phone\":\"+7 4212 21-45-12\",\"country_code\":\"7\",\"city_code\":\"4212\",\"number\":\"214512\",\"extension\":null,\"provider\":\"ПАО \\\"Ростелеком\\\"\",\"region\":\"Хабаровский\",\"timezone\":\"UTC+10\",\"qc_conflict\":0,\"qc\":0}]",
		             TestCasesSuccessPhone::matcherKhabarovsk5),
		KHABAROVSK_6("4212721234",
		             "[{\"source\":\"4212721234\",\"type\":\"Стационарный\",\"phone\":\"+7 4212 72-12-34\",\"country_code\":\"7\",\"city_code\":\"4212\",\"number\":\"721234\",\"extension\":null,\"provider\":\"ООО \\\"Телефонная компания Востоктелеком\\\"\",\"region\":\"Хабаровский\",\"timezone\":\"UTC+10\",\"qc_conflict\":0,\"qc\":0}]",
		             TestCasesSuccessPhone::matcherKhabarovsk6),
		KHABAROVSK_7("4212262000",
		             "[{\"source\":\"4212262000\",\"type\":\"Стационарный\",\"phone\":\"+7 4212 26-20-00\",\"country_code\":\"7\",\"city_code\":\"4212\",\"number\":\"262000\",\"extension\":null,\"provider\":\"АО \\\"ХАБАРОВСКИЙ АЭРОПОРТ\\\"\",\"region\":\"Хабаровский\",\"timezone\":\"UTC+10\",\"qc_conflict\":0,\"qc\":0}]",
		             TestCasesSuccessPhone::matcherKhabarovsk7),
		KHABAR_MOBILE_1("+79141523271",
		                "[{\"source\":\"+79141523271\",\"type\":\"Мобильный\",\"phone\":\"+7 914 152-32-71\",\"country_code\":\"7\",\"city_code\":\"914\",\"number\":\"1523271\",\"extension\":null,\"provider\":\"ПАО \\\"Мобильные ТелеСистемы\\\"\",\"region\":\"Хабаровский край\",\"timezone\":\"UTC+10\",\"qc_conflict\":0,\"qc\":0}]",
		                TestCasesSuccessPhone::matcherKhabarMobile1),
		KHABAR_MOBILE_2("+79243117001",
		                "[{\"source\":\"+79243117001\",\"type\":\"Мобильный\",\"phone\":\"+7 924 311-70-01\",\"country_code\":\"7\",\"city_code\":\"924\",\"number\":\"3117001\",\"extension\":null,\"provider\":\"ПАО \\\"МегаФон\\\"\",\"region\":\"Хабаровский край\",\"timezone\":\"UTC+10\",\"qc_conflict\":0,\"qc\":0}]",
		                TestCasesSuccessPhone::matcherKhabarMobile2),
		EAO_1("8 (42622) 2-13-88",
		      "[{\"source\":\"8 (42622) 2-13-88\",\"type\":\"Стационарный\",\"phone\":\"+7 42622 2-13-88\",\"country_code\":\"7\",\"city_code\":\"42622\",\"number\":\"21388\",\"extension\":null,\"provider\":\"ПАО \\\"Ростелеком\\\"\",\"region\":\"Еврейская\",\"timezone\":\"UTC+10\",\"qc_conflict\":0,\"qc\":0}]",
		      TestCasesSuccessPhone::matcherEao1),
		MOSCOW_1("(495) 261-63-66",
		         "[{\"source\":\"(495) 261-63-66\",\"type\":\"Стационарный\",\"phone\":\"+7 495 261-63-66\",\"country_code\":\"7\",\"city_code\":\"495\",\"number\":\"2616366\",\"extension\":null,\"provider\":\"ОАО \\\"МГТС\\\"\",\"region\":\"Москва\",\"timezone\":\"UTC+3\",\"qc_conflict\":0,\"qc\":0}]",
		         TestCasesSuccessPhone::matcherMoscow1),
		MOSCOW_2("+74999992412",
		         "[{\"source\":\"+74999992412\",\"type\":\"Стационарный\",\"phone\":\"+7 499 999-24-12\",\"country_code\":\"7\",\"city_code\":\"499\",\"number\":\"9992412\",\"extension\":null,\"provider\":\"ЗАО \\\"ГЛОБУС-ТЕЛЕКОМ\\\"\",\"region\":\"Москва\",\"timezone\":\"UTC+3\",\"qc_conflict\":0,\"qc\":0}]",
		         TestCasesSuccessPhone::matcherMoscow2),
		MOSCOW_MOBILE_1("(999) 841-59-07",
		                "[{\"source\":\"(999) 841-59-07\",\"type\":\"Мобильный\",\"phone\":\"+7 999 841-59-07\",\"country_code\":\"7\",\"city_code\":\"999\",\"number\":\"8415907\",\"extension\":null,\"provider\":\"ПАО \\\"Вымпел-Коммуникации\\\"\",\"region\":\"Москва и Московская область\",\"timezone\":\"UTC+3\",\"qc_conflict\":0,\"qc\":0}]",
		                TestCasesSuccessPhone::matcherMoscowMobile1),
		MOSCOW_MOBILE_2("(925) 888-1263",
		                "[{\"source\":\"(925) 888-1263\",\"type\":\"Мобильный\",\"phone\":\"+7 925 888-12-63\",\"country_code\":\"7\",\"city_code\":\"925\",\"number\":\"8881263\",\"extension\":null,\"provider\":\"ПАО \\\"МегаФон\\\"\",\"region\":\"Москва и Московская область\",\"timezone\":\"UTC+3\",\"qc_conflict\":0,\"qc\":0}]",
		                TestCasesSuccessPhone::matcherMoscowMobile2),
		MOSCOW_MOBILE_3("(985) 483-4111",
		                "[{\"source\":\"(985) 483-4111\",\"type\":\"Мобильный\",\"phone\":\"+7 985 483-41-11\",\"country_code\":\"7\",\"city_code\":\"985\",\"number\":\"4834111\",\"extension\":null,\"provider\":\"ПАО \\\"Мобильные ТелеСистемы\\\"\",\"region\":\"Москва и Московская область\",\"timezone\":\"UTC+3\",\"qc_conflict\":0,\"qc\":0}]",
		                TestCasesSuccessPhone::matcherMoscowMobile3),
		MOSCOW_MOBILE_4("(977) 792-0765",
		                "[{\"source\":\"(977) 792-0765\",\"type\":\"Мобильный\",\"phone\":\"+7 977 792-07-65\",\"country_code\":\"7\",\"city_code\":\"977\",\"number\":\"7920765\",\"extension\":null,\"provider\":\"ООО \\\"Т2 Мобайл\\\"\",\"region\":\"Москва и Московская область\",\"timezone\":\"UTC+3\",\"qc_conflict\":0,\"qc\":0}]",
		                TestCasesSuccessPhone::matcherMoscowMobile4),
		SPB_1("+7 (812) 576-41-11",
		      "[{\"source\":\"+7 (812) 576-41-11\",\"type\":\"Стационарный\",\"phone\":\"+7 812 576-41-11\",\"country_code\":\"7\",\"city_code\":\"812\",\"number\":\"5764111\",\"extension\":null,\"provider\":\"СПб ГУП \\\"АТС Смольного\\\"\",\"region\":\"Санкт-Петербург\",\"timezone\":\"UTC+3\",\"qc_conflict\":0,\"qc\":0}]",
		      TestCasesSuccessPhone::matcherSpb1),
		SPB_MOBILE_1("953 346-46-46",
		             "[{\"source\":\"953 346-46-46\",\"type\":\"Мобильный\",\"phone\":\"+7 953 346-46-46\",\"country_code\":\"7\",\"city_code\":\"953\",\"number\":\"3464646\",\"extension\":null,\"provider\":\"ПАО \\\"МегаФон\\\"\",\"region\":\"Санкт-Петербург и Ленинградская область\",\"timezone\":\"UTC+3\",\"qc_conflict\":0,\"qc\":0}]",
		             TestCasesSuccessPhone::matcherSpbMobile1),
		SPB_SIP_1("812 292 09 09",
		          "[{\"source\":\"812 292 09 09\",\"type\":\"Стационарный\",\"phone\":\"+7 812 292-09-09\",\"country_code\":\"7\",\"city_code\":\"812\",\"number\":\"2920909\",\"extension\":null,\"provider\":\"ООО \\\"ТЕЛЕСТОР\\\"\",\"region\":\"Санкт-Петербург\",\"timezone\":\"UTC+3\",\"qc_conflict\":0,\"qc\":0}]",
		          TestCasesSuccessPhone::matcherSpbSip1),
		NOVOSIB_1("+7 (383) 227-40-40",
		          "[{\"source\":\"+7 (383) 227-40-40\",\"type\":\"Стационарный\",\"phone\":\"+7 383 227-40-40\",\"country_code\":\"7\",\"city_code\":\"383\",\"number\":\"2274040\",\"extension\":null,\"provider\":\"ПАО \\\"Ростелеком\\\"\",\"region\":\"Новосибирская\",\"timezone\":\"UTC+7\",\"qc_conflict\":0,\"qc\":0}]",
		          TestCasesSuccessPhone::matcherNovosib1),
		NOVOSIB_MOBILE_1("89133990909",
		                 "[{\"source\":\"89133990909\",\"type\":\"Мобильный\",\"phone\":\"+7 913 399-09-09\",\"country_code\":\"7\",\"city_code\":\"913\",\"number\":\"3990909\",\"extension\":null,\"provider\":\"ПАО \\\"Мобильные ТелеСистемы\\\"\",\"region\":\"Новосибирская область\",\"timezone\":\"UTC+7\",\"qc_conflict\":0,\"qc\":0}]",
		                 TestCasesSuccessPhone::matcherNovosibMobile1),
		IN_TTK_1("8(800)775-00-00",
		         "[{\"source\":\"8(800)775-00-00\",\"type\":\"Колл-центр\",\"phone\":\"+7 800 775-00-00\",\"country_code\":\"7\",\"city_code\":\"800\",\"number\":\"7750000\",\"extension\":null,\"provider\":\"АО \\\"Компания ТрансТелеКом\\\"\",\"region\":null,\"timezone\":null,\"qc_conflict\":0,\"qc\":0}]",
		         TestCasesSuccessPhone::matcherInTTK1),
		INVARIANTS_1("8 4212 450045, 8 914 152 7123",
		             "[{\"source\":\"8 4212 450045, 8 914 152 7123\",\"type\":\"Стационарный\",\"phone\":\"+7 4212 45-00-45\",\"country_code\":\"7\",\"city_code\":\"4212\",\"number\":\"450045\",\"extension\":null,\"provider\":\"АО \\\"Рэдком-Интернет\\\"\",\"region\":\"Хабаровский\",\"timezone\":\"UTC+10\",\"qc_conflict\":0,\"qc\":3}]",
		             TestCasesSuccessPhone::matcherInvariants1),
		UNKNOWN_1("450045",
		          "[{\"source\":\"450045\",\"type\":\"Неизвестный\",\"phone\":null,\"country_code\":null,\"city_code\":null,\"number\":null,\"extension\":null,\"provider\":null,\"region\":null,\"timezone\":null,\"qc_conflict\":0,\"qc\":1}]",
		          TestCasesSuccessPhone::matcherUnknown1),
		FOREIGN_1("+1 (703) 482-0623",
		          "[{\"source\":\"+1 (703) 482-0623\",\"type\":\"Неизвестный\",\"phone\":null,\"country_code\":null,\"city_code\":null,\"number\":null,\"extension\":null,\"provider\":null,\"region\":null,\"timezone\":null,\"qc_conflict\":0,\"qc\":1}]",
		          TestCasesSuccessPhone::matcherUnknown1),
		UNPARSEABLE_1("qwe",
		              "[{\"source\":\"qwe\",\"type\":null,\"phone\":null,\"country_code\":null,\"city_code\":null,\"number\":null,\"extension\":null,\"provider\":null,\"region\":null,\"timezone\":null,\"qc_conflict\":0,\"qc\":2}]",
		              TestCasesSuccessPhone::matcherUnparseable);

		private final String sourcePattern;
		private final String responseBody;
		private final Function<String, Matcher<Phone>> matcher;

		SamplePhones(final String sourcePattern, final String responseBody, final Function<String, Matcher<Phone>> matcher) {
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

		public Matcher<Phone> getMatcher() {
			return matcher != null ? matcher.apply(sourcePattern) : nullValue(Phone.class);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	// 4212450045
	// 84212450045
	// +74212450045
	private static Matcher<Phone> matcherKhabarovsk1(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Стационарный"))
				.withProperty("phone", is("+7 4212 45-00-45"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("4212"))
				.withProperty("number", is("450045"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("АО \"Рэдком-Интернет\""))
				.withProperty("region", is("Хабаровский"))
				.withProperty("timezone", is("UTC+10"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// 8 (4212) 450045 доб.300
	private static Matcher<Phone> matcherKhabarovsk4(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Стационарный"))
				.withProperty("phone", is("+7 4212 45-00-45 доб. 300"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("4212"))
				.withProperty("number", is("450045"))
				.withProperty("extension", is("300"))
				.withProperty("provider", is("АО \"Рэдком-Интернет\""))
				.withProperty("region", is("Хабаровский"))
				.withProperty("timezone", is("UTC+10"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// 4212214512
	private static Matcher<Phone> matcherKhabarovsk5(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Стационарный"))
				.withProperty("phone", is("+7 4212 21-45-12"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("4212"))
				.withProperty("number", is("214512"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("ПАО \"Ростелеком\""))
				.withProperty("region", is("Хабаровский"))
				.withProperty("timezone", is("UTC+10"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// 4212721234
	private static Matcher<Phone> matcherKhabarovsk6(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Стационарный"))
				.withProperty("phone", is("+7 4212 72-12-34"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("4212"))
				.withProperty("number", is("721234"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("ООО \"Телефонная компания Востоктелеком\""))
				.withProperty("region", is("Хабаровский"))
				.withProperty("timezone", is("UTC+10"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// 4212262000
	private static Matcher<Phone> matcherKhabarovsk7(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Стационарный"))
				.withProperty("phone", is("+7 4212 26-20-00"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("4212"))
				.withProperty("number", is("262000"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("АО \"ХАБАРОВСКИЙ АЭРОПОРТ\""))
				.withProperty("region", is("Хабаровский"))
				.withProperty("timezone", is("UTC+10"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// +79141523271
	private static Matcher<Phone> matcherKhabarMobile1(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Мобильный"))
				.withProperty("phone", is("+7 914 152-32-71"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("914"))
				.withProperty("number", is("1523271"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("ПАО \"Мобильные ТелеСистемы\""))
				.withProperty("region", is("Хабаровский край"))
				.withProperty("timezone", is("UTC+10"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	//	+79243117001
	private static Matcher<Phone> matcherKhabarMobile2(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Мобильный"))
				.withProperty("phone", is("+7 924 311-70-01"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("924"))
				.withProperty("number", is("3117001"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("ПАО \"МегаФон\""))
				.withProperty("region", is("Хабаровский край"))
				.withProperty("timezone", is("UTC+10"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// 8 (42622) 2-13-88
	private static Matcher<Phone> matcherEao1(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Стационарный"))
				.withProperty("phone", is("+7 42622 2-13-88"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("42622"))
				.withProperty("number", is("21388"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("ПАО \"Ростелеком\""))
				.withProperty("region", is("Еврейская"))
				.withProperty("timezone", is("UTC+10"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// (495) 261-63-66
	private static Matcher<Phone> matcherMoscow1(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Стационарный"))
				.withProperty("phone", is("+7 495 261-63-66"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("495"))
				.withProperty("number", is("2616366"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("ОАО \"МГТС\""))
				.withProperty("region", is("Москва"))
				.withProperty("timezone", is("UTC+3"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// +74999992412
	private static Matcher<Phone> matcherMoscow2(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Стационарный"))
				.withProperty("phone", is("+7 499 999-24-12"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("499"))
				.withProperty("number", is("9992412"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("ЗАО \"ГЛОБУС-ТЕЛЕКОМ\""))
				.withProperty("region", is("Москва"))
				.withProperty("timezone", is("UTC+3"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// (999) 841-59-07
	private static Matcher<Phone> matcherMoscowMobile1(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Мобильный"))
				.withProperty("phone", is("+7 999 841-59-07"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("999"))
				.withProperty("number", is("8415907"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("ПАО \"Вымпел-Коммуникации\""))
				.withProperty("region", is("Москва и Московская область"))
				.withProperty("timezone", is("UTC+3"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// (925) 888-1263
	private static Matcher<Phone> matcherMoscowMobile2(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Мобильный"))
				.withProperty("phone", is("+7 925 888-12-63"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("925"))
				.withProperty("number", is("8881263"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("ПАО \"МегаФон\""))
				.withProperty("region", is("Москва и Московская область"))
				.withProperty("timezone", is("UTC+3"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// (985) 483-4111
	private static Matcher<Phone> matcherMoscowMobile3(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Мобильный"))
				.withProperty("phone", is("+7 985 483-41-11"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("985"))
				.withProperty("number", is("4834111"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("ПАО \"Мобильные ТелеСистемы\""))
				.withProperty("region", is("Москва и Московская область"))
				.withProperty("timezone", is("UTC+3"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// (977) 792-0765
	private static Matcher<Phone> matcherMoscowMobile4(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Мобильный"))
				.withProperty("phone", is("+7 977 792-07-65"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("977"))
				.withProperty("number", is("7920765"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("ООО \"Т2 Мобайл\""))
				.withProperty("region", is("Москва и Московская область"))
				.withProperty("timezone", is("UTC+3"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// +7 (812) 576-41-11
	private static Matcher<Phone> matcherSpb1(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Стационарный"))
				.withProperty("phone", is("+7 812 576-41-11"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("812"))
				.withProperty("number", is("5764111"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("СПб ГУП \"АТС Смольного\""))
				.withProperty("region", is("Санкт-Петербург"))
				.withProperty("timezone", is("UTC+3"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// 953 346-46-46
	private static Matcher<Phone> matcherSpbMobile1(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Мобильный"))
				.withProperty("phone", is("+7 953 346-46-46"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("953"))
				.withProperty("number", is("3464646"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("ПАО \"МегаФон\""))
				.withProperty("region", is("Санкт-Петербург и Ленинградская область"))
				.withProperty("timezone", is("UTC+3"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// 812 292 09 09
	private static Matcher<Phone> matcherSpbSip1(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Стационарный"))
				.withProperty("phone", is("+7 812 292-09-09"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("812"))
				.withProperty("number", is("2920909"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("ООО \"ТЕЛЕСТОР\""))
				.withProperty("region", is("Санкт-Петербург"))
				.withProperty("timezone", is("UTC+3"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// +7 (383) 227-40-40
	private static Matcher<Phone> matcherNovosib1(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Стационарный"))
				.withProperty("phone", is("+7 383 227-40-40"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("383"))
				.withProperty("number", is("2274040"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("ПАО \"Ростелеком\""))
				.withProperty("region", is("Новосибирская"))
				.withProperty("timezone", is("UTC+7"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// 89133990909
	private static Matcher<Phone> matcherNovosibMobile1(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Мобильный"))
				.withProperty("phone", is("+7 913 399-09-09"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("913"))
				.withProperty("number", is("3990909"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("ПАО \"Мобильные ТелеСистемы\""))
				.withProperty("region", is("Новосибирская область"))
				.withProperty("timezone", is("UTC+7"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// 8(800)775-00-00
	private static Matcher<Phone> matcherInTTK1(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Колл-центр"))
				.withProperty("phone", is("+7 800 775-00-00"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("800"))
				.withProperty("number", is("7750000"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("АО \"Компания ТрансТелеКом\""))
				.withProperty("region", is(nullValue(String.class)))
				.withProperty("timezone", is(nullValue(String.class)))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.FULL));
	}

	// 8 4212 450045, 8 914 152 7123
	private static Matcher<Phone> matcherInvariants1(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Стационарный"))
				.withProperty("phone", is("+7 4212 45-00-45"))
				.withProperty("countryCode", is("7"))
				.withProperty("cityCode", is("4212"))
				.withProperty("number", is("450045"))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is("АО \"Рэдком-Интернет\""))
				.withProperty("region", is("Хабаровский"))
				.withProperty("timezone", is("UTC+10"))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.INVARIANTS));
	}

	// 450045
	// +1 (703) 482-0623
	// qwe
	private static Matcher<Phone> matcherUnknown1(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is("Неизвестный"))
				.withProperty("phone", is(nullValue(String.class)))
				.withProperty("countryCode", is(nullValue(String.class)))
				.withProperty("cityCode", is(nullValue(String.class)))
				.withProperty("number", is(nullValue(String.class)))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is(nullValue(String.class)))
				.withProperty("region", is(nullValue(String.class)))
				.withProperty("timezone", is(nullValue(String.class)))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.PARTIAL));
	}

	// qwe
	private static Matcher<Phone> matcherUnparseable(final String s) {
		return pojo(Phone.class)
				.withProperty("source", is(equalTo(s)))
				.withProperty("type", is(nullValue(String.class)))
				.withProperty("phone", is(nullValue(String.class)))
				.withProperty("countryCode", is(nullValue(String.class)))
				.withProperty("cityCode", is(nullValue(String.class)))
				.withProperty("number", is(nullValue(String.class)))
				.withProperty("extension", is(nullValue(String.class)))
				.withProperty("provider", is(nullValue(String.class)))
				.withProperty("region", is(nullValue(String.class)))
				.withProperty("timezone", is(nullValue(String.class)))
				.withProperty("qcConflict", is(QcConflict.EXACT))
				.withProperty("qc", is(QcPhone.UNRECOGNIZED));
	}

	// =================================================================================================================

	public static void successTest(final DaDataClient dadata, final String sourcePattern, final Matcher<Phone> matcher) {
		System.out.println("source phone: " + sourcePattern);
		final Phone a = dadata.cleanPhone(sourcePattern);
		System.out.println("cleaned phone: " + a);
		assertThat(a, is(matcher));
	}
}
