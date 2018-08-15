/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Arrays;

/**
 * Geoposition precision level.
 *
 * @author boris
 */
// JsonProperty/JsonValue does not work on enums when deserialize from json numerical types.
// Deserialization is done by ordinals instead, which is definitely not what we wants here.
// see https://github.com/FasterXML/jackson-databind/issues/1850
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
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
	/** Catch-all constant for unrecognized response contents */
	@JsonEnumDefaultValue
	UNKNOWN(null);

	@Nullable private final Integer jsonValue;

	private boolean equalsTo(@NonNull final Integer value) {
		return value.equals(jsonValue);
	}

	@SuppressWarnings("unused")
	@Nullable
	@JsonCreator
	private static QcGeo jsonCreator(final Integer s) {
		return s == null ? null : Arrays.stream(values()).filter(v -> v.equalsTo(s)).findAny().orElse(UNKNOWN);
	}
}
