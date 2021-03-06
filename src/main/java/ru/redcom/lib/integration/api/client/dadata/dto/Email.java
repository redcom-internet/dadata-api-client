/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.redcom.lib.integration.api.client.dadata.types.QcEmail;

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

/**
 * Cleaned E-mail address container.
 *
 * @author boris
 */
@SuppressWarnings("unused")
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Email extends ResponseItem {
	private String email;

	@JsonProperty(required = true)
	private QcEmail qc;

	// This class is not instantiable
	private Email() {
	}
}
