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
import ru.redcom.lib.integration.api.client.dadata.types.QcPassport;

/*
Название	Длина	Описание
source	100	Исходная серия и номер одной строкой
series	20	Серия
number	20	Номер
qc	5	Код проверки

[
  {
    "source": "4509 235857",
    "series": "45 09",
    "number": "235857",
    "qc": 0
  }
]
*/

/**
 * Cleaned Passport Number container.
 *
 * @author boris
 */
@SuppressWarnings("unused")
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Passport extends ResponseItem {
	private String series;
	private String number;

	@JsonProperty(required = true)
	private QcPassport qc;

	// This class is not instantiable
	private Passport() {
	}
}
