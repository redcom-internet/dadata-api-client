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
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Arrays;

/** Locatino capital/center status */
// JsonProperty/JsonValue does not work on enums when deserializing from json numerical types.
// Deserialization is done by ordinals instead, which is definitely not what we wants here.
// see https://github.com/FasterXML/jackson-databind/issues/1850
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CapitalMarker {
	/*
	Признак центра района или региона
	1 — центр района (Московская обл, Одинцовский р-н, г Одинцово);
	2 — центр региона (Новосибирская обл, г Новосибирск);
	3 — центр района и региона (Томская обл, г Томск);
	4 — центральный район региона (Тюменская обл, Тюменский р-н);
	0 — ничего из перечисленного (Московская обл, г Балашиха);
	 */
	/** Area center */
	@JsonProperty("1")
	AREA_CENTER(1),
	/** Region center */
	@JsonProperty("2")
	REGION_CENTER(2),
	/** Both Area and Region center simultaneously */
	@JsonProperty("3")
	AREA_REGION_CENTER(3),
	/** Central area of the region */
	@JsonProperty("4")
	REGION_CENTRAL_AREA(4),
	/** Other cases or location is not a center */
	@JsonProperty("0")
	OTHER(0),
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
	private static CapitalMarker jsonCreator(final Integer s) {
		return s == null ? null : Arrays.stream(values()).filter(v -> v.equalsTo(s)).findAny().orElse(UNKNOWN);
	}
}
