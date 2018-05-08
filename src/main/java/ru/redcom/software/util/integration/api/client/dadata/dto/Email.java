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
import ru.redcom.software.util.integration.api.client.dadata.types.QcEmail;

import java.io.Serializable;

/*
Название	Длина	Описание
source	100	Исходный email
email	100	Стандартизованный email
qc	5	Код проверки

[
  {
    "source": "serega@yandex/ru",
    "email": "serega@yandex.ru",
    "qc": 4
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
public class Email implements Serializable {
	@JsonProperty(required = true)
	private String source;

	private String email;

	@JsonProperty(required = true)
	private QcEmail qc;
}
