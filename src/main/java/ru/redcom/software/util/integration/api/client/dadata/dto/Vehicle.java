/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.redcom.software.util.integration.api.client.dadata.types.QcVehicle;

import java.io.Serializable;

/*
Название	Длина	Описание
source	100	Исходное значение
result	100	Стандартизованное значение
brand	50	Марка
model	50	Модель
qc	5	Код проверки

[
  {
    "source": "форд фокус",
    "result": "FORD FOCUS",
    "brand": "FORD",
    "model": "FOCUS",
    "qc": 0
  }
]
*/
// Property annotation with "required" does not currently enforce mandatoriness on fields,
// see https://github.com/FasterXML/jackson-databind/issues/230
@SuppressWarnings("unused")
@Getter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vehicle implements Serializable {
	private final static String STRUCTURE_LITERAL = "VEHICLE";

	@JsonProperty(required = true)
	private String source;
	private String result;

	private String brand;
	private String model;

	@JsonProperty(required = true)
	private QcVehicle qc;
}
