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
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE)
@RequiredArgsConstructor
public enum QcHouse {
	/*
	Признак наличия дома в ФИАС (qc_house) — уточняет вероятность успешной доставки письма:

	Код qc_house	Вероятность доставки	Описание
	2	Высокая	Дом найден в ФИАС по точному совпадению
	3	Средняя	В ФИАС найден похожий дом; различие в литере, корпусе или строении
	4	Средняя	Дом найден в ФИАС по диапазону
	10	Низкая	Дом не найден в ФИАС
	*/
	@JsonProperty("2")
	EXACT(2, DeliveryProbability.HIGH),
	@JsonProperty("3")
	SIMILAR(3, DeliveryProbability.MODERATE),
	@JsonProperty("4")
	RANGE(4, DeliveryProbability.MODERATE),
	@JsonProperty("10")
	NOT_FOUND(10, DeliveryProbability.LOW),
	@JsonEnumDefaultValue
	UNKNOWN(null, DeliveryProbability.LOW);


	public enum DeliveryProbability {
		HIGH, MODERATE, LOW
	}

	@Nullable private final Integer jsonValue;
	@Nonnull private final DeliveryProbability deliveryProbability;

	private boolean equalsTo(@Nonnull final Integer value) {
		return value.equals(jsonValue);
	}

	@SuppressWarnings("unused")
	@JsonCreator
	@Nullable
	private static QcHouse jsonCreator(final Integer s) {
		return s == null ? null : Arrays.stream(values()).filter(v -> v.equalsTo(s)).findAny().orElse(UNKNOWN);
	}

	@Nonnull
	public DeliveryProbability getPostalProbability() {
		return deliveryProbability;
	}
}
