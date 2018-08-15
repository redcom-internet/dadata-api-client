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
 * Phone Number parsing quality code.
 *
 * @author boris
 */
// JsonProperty/JsonValue does not work on enums when deserialize from json numerical types.
// Deserialization is done by ordinals instead, which is definitely not what we wants here.
// see https://github.com/FasterXML/jackson-databind/issues/1850
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum QcPhone {
	/*
	Код проверки (qc) — нужно ли вручную проверить распознанный телефон:

	Код qc	Описание	Нужно проверить вручную?
	0	Телефон распознан уверенно	Нет
	2	Телефон пустой или заведомо «мусорный»	Нет
	1	Телефон распознан с допущениями или не распознан	Да
	3	Обнаружено несколько телефонов, распознан первый	Да
	 */
	@JsonProperty("0")
	FULL(0),
	@JsonProperty("1")
	PARTIAL(1),
	@JsonProperty("2")
	UNRECOGNIZED(2),
	@JsonProperty("3")
	INVARIANTS(3),
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
	private static QcPhone jsonCreator(final Integer s) {
		return s == null ? null : Arrays.stream(values()).filter(v -> v.equalsTo(s)).findAny().orElse(UNKNOWN);
	}

	/**
	 * Is manual verification of response desirable?
	 *
	 * @return <code>true</code> if manual verification should be done, <code>false</code> otherwise
	 */
	public boolean isManualVerificationRequired() {
		return this == PARTIAL || this == INVARIANTS;
	}
}
