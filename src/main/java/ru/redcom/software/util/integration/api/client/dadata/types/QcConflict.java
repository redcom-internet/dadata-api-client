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

/**
 * Phone and location correlation status.
 *
 * @author boris
 */
// JsonProperty/JsonValue does not work on enums when deserialize from json numerical types.
// Deserialization is done by ordinals instead, which is definitely not what we wants here.
// see https://github.com/FasterXML/jackson-databind/issues/1850
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum QcConflict {
	/*
	Признак конфликта телефона с адресом (qc_conflict) — указал ли клиент телефон, соответствующий его адресу. Удобно для проверки уровня риска:

	Код qc_conflict	Описание
	0	Телефон соответствует адресу
	2	Города адреса и телефона отличаются
	3	Регионы адреса и телефона отличаются
	 */
	@JsonProperty("0")
	EXACT(0),
	@JsonProperty("2")
	CITY_DIFFERS(2),
	@JsonProperty("3")
	REGION_DIFFERS(3),
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
	private static QcConflict jsonCreator(final Integer s) {
		return s == null ? null : Arrays.stream(values()).filter(v -> v.equalsTo(s)).findAny().orElse(UNKNOWN);
	}
}
