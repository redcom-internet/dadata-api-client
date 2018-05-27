/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.redcom.software.util.integration.api.client.dadata.types.QcBirthDate;

import java.time.LocalDate;

/*
Название	Длина	Описание
source	100	Исходная дата
birthdate	100	Стандартизованная дата
qc	5	Код проверки

[
  {
    "source": "24/3/12",
    "birthdate": "24.03.2012",
    "qc": 1
  }
]
*/
// Property annotation with "required" does not currently enforce mandatoriness on fields,
// see https://github.com/FasterXML/jackson-databind/issues/230
@SuppressWarnings("unused")
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BirthDate extends ResponseItem {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
	private LocalDate birthdate;

	@JsonProperty(required = true)
	private QcBirthDate qc;
}
