/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/** Address completeness level */
// JsonProperty/JsonValue does not work on enums when deserializing from json numerical types.
// Deserialization is done by ordinals instead, which is definitely not what we wants here.
// see https://github.com/FasterXML/jackson-databind/issues/1850
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum QcAddressComplete {
	/*
	Код пригодности к рассылке (qc_complete) — годится ли адрес для доставки корреспонденции:

	Код qc_complete	Подходит для рассылки?	Описание
	0	Да	Пригоден для почтовой рассылки
	10	Под вопросом	Дома нет в ФИАС
	5	Под вопросом	Нет квартиры. Подходит для юридических лиц или частных владений
	8	Под вопросом	До почтового отделения — абонентский ящик или адрес до востребования. Подходит для писем, но не для курьерской доставки.
	9	Под вопросом	Сначала проверьте, правильно ли Дадата разобрала исходный адрес
	1	Нет	Нет региона
	2	Нет	Нет города
	3	Нет	Нет улицы
	4	Нет	Нет дома
	6	Нет	Адрес неполный
	7	Нет	Иностранный адрес
	*/
	@JsonProperty("0")
	ADDRESS_COMPLETE(0, PostalSuitability.YES),
	@JsonProperty("1")
	NO_REGION(1, PostalSuitability.NO),
	@JsonProperty("2")
	NO_CITY(2, PostalSuitability.NO),
	@JsonProperty("3")
	NO_STREET(3, PostalSuitability.NO),
	@JsonProperty("4")
	NO_HOUSE(4, PostalSuitability.NO),
	@JsonProperty("5")
	NO_FLAT(5, PostalSuitability.MAYBE),
	@JsonProperty("6")
	ADDRESS_INCOMPLETE(6, PostalSuitability.NO),
	@JsonProperty("7")
	FOREIGN_ADDRESS(7, PostalSuitability.NO),
	@JsonProperty("8")
	POSTAL_BOX(8, PostalSuitability.MAYBE),
	@JsonProperty("9")
	VERIFY_PARSING(9, PostalSuitability.MAYBE),
	@JsonProperty("10")
	HOUSE_NOT_IN_FIAS(10, PostalSuitability.MAYBE),
	/** Catch-all constant for unrecognized response contents */
	@JsonEnumDefaultValue
	UNKNOWN(null, PostalSuitability.NO);

	/** Address suitability for postal services */
	public enum PostalSuitability {
		/** Address is likely suitable for postage delivery */
		YES,
		/** Address may be used for postage, but delivery cannot be guaranteed */
		MAYBE,
		/** Address is not suitable for postage delivery */
		NO
	}

	@Nullable private final Integer jsonValue;
	@Nonnull private final PostalSuitability postalSuitability;

	private boolean equalsTo(@Nonnull final Integer value) {
		return value.equals(jsonValue);
	}

	@SuppressWarnings("unused")
	@Nullable
	@JsonCreator
	private static QcAddressComplete jsonCreator(final Integer s) {
		return s == null ? null : Arrays.stream(values()).filter(v -> v.equalsTo(s)).findAny().orElse(UNKNOWN);
	}

	/**
	 * Get address's postal suitability level.
	 *
	 * @return Postal suitability estimation
	 */
	@Nonnull
	public PostalSuitability getPostalSuitability() {
		return postalSuitability;
	}
}
