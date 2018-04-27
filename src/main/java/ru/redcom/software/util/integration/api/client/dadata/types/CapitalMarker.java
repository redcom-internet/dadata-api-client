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
public enum CapitalMarker {
	/*
	Признак центра района или региона
	1 — центр района (Московская обл, Одинцовский р-н, г Одинцово);
	2 — центр региона (Новосибирская обл, г Новосибирск);
	3 — центр района и региона (Томская обл, г Томск);
	4 — центральный район региона (Тюменская обл, Тюменский р-н);
	0 — ничего из перечисленного (Московская обл, г Балашиха);
	 */
	@JsonProperty("1")
	AREA_CENTER(1),
	@JsonProperty("2")
	REGION_CENTER(2),
	@JsonProperty("3")
	AREA_REGION_CENTER(3),
	@JsonProperty("4")
	REGION_CENTRAL_AREA(4),
	@JsonProperty("0")
	OTHER(0),
	@JsonEnumDefaultValue
	UNKNOWN(null);

	@Nullable private final Integer jsonValue;

	private boolean equalsTo(@Nonnull final Integer value) {
		return value.equals(jsonValue);
	}

	@JsonCreator
	@Nonnull
	private static CapitalMarker jsonCreator(final int s) {
		return Arrays.stream(values()).filter(v -> v.equalsTo(s)).findAny().orElse(UNKNOWN);
	}

}
