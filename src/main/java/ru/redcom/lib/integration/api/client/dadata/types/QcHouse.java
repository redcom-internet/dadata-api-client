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
 * House lookup level in FIAS directory.
 *
 * @author boris
 */
// JsonProperty/JsonValue does not work on enums when deserialize from json numerical types.
// Deserialization is done by ordinals instead, which is definitely not what we wants here.
// see https://github.com/FasterXML/jackson-databind/issues/1850
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum QcHouse {
	@JsonProperty("2")
	EXACT(2, DeliveryProbability.HIGH)/*
	Признак наличия дома в ФИАС (qc_house) — уточняет вероятность успешной доставки письма:

	Код qc_house	Вероятность доставки	Описание
	2	Высокая	Дом найден в ФИАС по точному совпадению
	3	Средняя	В ФИАС найден похожий дом; различие в литере, корпусе или строении
	4	Средняя	Дом найден в ФИАС по диапазону
	10	Низкая	Дом не найден в ФИАС
	*/,
	@JsonProperty("10")
	NOT_FOUND(10, DeliveryProbability.LOW),
	@JsonProperty("4")
	RANGE(4, DeliveryProbability.MODERATE),
	@JsonProperty("3")
	SIMILAR(3, DeliveryProbability.MODERATE),
	/** Catch-all constant for unrecognized response contents */
	@JsonEnumDefaultValue
	UNKNOWN(null, DeliveryProbability.LOW);

	/** Estimated postal delivery probability level */
	public enum DeliveryProbability {
		HIGH, MODERATE, LOW
	}

	@Nullable private final Integer jsonValue;
	@NonNull private final DeliveryProbability deliveryProbability;

	private boolean equalsTo(@NonNull final Integer value) {
		return value.equals(jsonValue);
	}

	@SuppressWarnings("unused")
	@Nullable
	@JsonCreator
	private static QcHouse jsonCreator(final Integer s) {
		return s == null ? null : Arrays.stream(values()).filter(v -> v.equalsTo(s)).findAny().orElse(UNKNOWN);
	}

	/**
	 * Get postal delivery estimation.
	 *
	 * @return Postal delivery probability level
	 */
	@NonNull
	public DeliveryProbability getPostalProbability() {
		return deliveryProbability;
	}
}
