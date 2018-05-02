/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.types;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

// JsonProperty/JsonValue does not work on enums when deserializing from json numerical types.
// Deserialization is done by ordinals instead, which is definitely not what we wants here.
// see https://github.com/FasterXML/jackson-databind/issues/1850
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE)
@RequiredArgsConstructor
public enum QcGeo {
	/*
	Код точности координат qc_geo — точность координат адреса для курьерской доставки:

	Код qc_geo	Описание
	0	Точные координаты
	1	Ближайший дом
	2	Улица
	3	Населенный пункт
	4	Город
	5	Координаты не определены
	 */
	@JsonProperty("0")
	EXACT(0),
	@JsonProperty("1")
	NEAREST_HOUSE(1),
	@JsonProperty("2")
	STREET(2),
	@JsonProperty("3")
	SETTLEMENT(3),
	@JsonProperty("4")
	CITY(4),
	@JsonProperty("5")
	UNDEFINED(5),
	@JsonEnumDefaultValue
	UNKNOWN(null);

	@Nullable private final Integer jsonValue;

	private boolean equalsTo(@Nonnull final Integer value) {
		return value.equals(jsonValue);
	}

	/* TODO cleanup
		@JsonCreator
		@Nonnull
		private static QcGeo jsonCreator(final int s) {
			return Arrays.stream(values()).filter(v -> v.equalsTo(s)).findAny().orElse(UNKNOWN);
		}
	*/
	@SuppressWarnings("unused")
	@JsonCreator
	@Nullable
	private static QcGeo jsonCreator(final Integer s) {
		return s == null ? null : Arrays.stream(values()).filter(v -> v.equalsTo(s)).findAny().orElse(UNKNOWN);
	}
}