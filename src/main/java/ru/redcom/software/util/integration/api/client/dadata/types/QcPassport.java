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
 * Passport number parsing quality code.
 *
 * @author boris
 */
// JsonProperty/JsonValue does not work on enums when deserialize from json numerical types.
// Deserialization is done by ordinals instead, which is definitely not what we wants here.
// see https://github.com/FasterXML/jackson-databind/issues/1850
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum QcPassport {
	/*
	Код проверки (qc) — действует паспорт или нет, по данным Федеральной миграционной службы:

	Код qc	Описание	Нужно проверить вручную?
	0	Действующий паспорт	Нет
	2	Исходное значение пустое	Нет
	1	Неправильный формат серии или номера	Да
	10	Недействительный паспорт	Да
	 */
	@JsonProperty("0")
	VALID(0),
	@JsonProperty("1")
	BAD_FORMAT(1),
	@JsonProperty("2")
	EMPTY(2),
	@JsonProperty("10")
	VOID(10),
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
	private static QcPassport jsonCreator(final Integer s) {
		return s == null ? null : Arrays.stream(values()).filter(v -> v.equalsTo(s)).findAny().orElse(UNKNOWN);
	}

	/**
	 * Is manual verification of response desirable?
	 *
	 * @return <code>true</code> if manual verification should be done, <code>false</code> otherwise
	 */
	public boolean isManualVerificationRequired() {
		return this == BAD_FORMAT || this == VOID;
	}
}
