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
public enum QcBirthDate {
	/*
	Код проверки (qc) — требуется ли вручную проверить распознанное значение:

	Код qc	Описание	Нужно проверить вручную?
	0	Исходное значение распознано уверенно	Нет
	2	Исходное значение пустое или заведомо «мусорное»	Нет
	1	Исходное значение распознано с допущениями или не распознано	Да
	*/
	@JsonProperty("0")
	FULL(0),
	@JsonProperty("1")
	PARTIAL(1),
	@JsonProperty("2")
	UNRECOGNIZED(2),
	@JsonEnumDefaultValue
	UNKNOWN(null);

	@Nullable private final Integer jsonValue;

	private boolean equalsTo(@Nonnull final Integer value) {
		return value.equals(jsonValue);
	}

	@SuppressWarnings("unused")
	@JsonCreator
	@Nullable
	private static QcBirthDate jsonCreator(final Integer s) {
		return s == null ? null : Arrays.stream(values()).filter(v -> v.equalsTo(s)).findAny().orElse(UNKNOWN);
	}

	public boolean isManualVerificationRequired() {
		return this == PARTIAL;
	}
}
