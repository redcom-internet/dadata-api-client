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

/** E-mail address parsing quality code */
// JsonProperty/JsonValue does not work on enums when deserializing from json numerical types.
// Deserialization is done by ordinals instead, which is definitely not what we wants here.
// see https://github.com/FasterXML/jackson-databind/issues/1850
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum QcEmail {
	/*
	Код проверки (qc) — подходит ли email для маркетинговой рассылки:

	Код qc	Описание	Нужно проверить вручную?
	0	Корректное значение
	Соответствует общепринятым правилам,
	реальное существование адреса не проверяется	Нет
	2	Пустое или заведомо «мусорное» значение	Нет
	3	«Одноразовый» адрес
	Домены 10minutemail.com, getairmail.com, temp-mail.ru и аналогичные	Нет
	1	Некорректное значение
	Не соответствует общепринятым правилам	Да
	4	Исправлены опечатки	Да
	 */
	@JsonProperty("0")
	VALID(0),
	@JsonProperty("1")
	INVALID(1),
	@JsonProperty("2")
	UNRECOGNIZED(2),
	@JsonProperty("3")
	INSTANT(3),
	@JsonProperty("4")
	CORRECTED(4),
	/** Catch-all constant for unrecognized response contents */
	@JsonEnumDefaultValue
	UNKNOWN(null);

	@Nullable private final Integer jsonValue;

	private boolean equalsTo(@Nonnull final Integer value) {
		return value.equals(jsonValue);
	}

	@SuppressWarnings("unused")
	@Nullable
	@JsonCreator
	private static QcEmail jsonCreator(final Integer s) {
		return s == null ? null : Arrays.stream(values()).filter(v -> v.equalsTo(s)).findAny().orElse(UNKNOWN);
	}

	/**
	 * Is manual verification of response desirable?
	 *
	 * @return <code>true</code> if manual verification should be done, <code>false</code> otherwise
	 */
	public boolean isManualVerificationRequired() {
		return this == INVALID || this == CORRECTED;
	}
}
