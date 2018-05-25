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
import ru.redcom.software.util.integration.api.client.dadata.types.QcPassport;

import java.io.Serializable;

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
// Property annotation with "required" does not currently enforce mandatoriness on fields,
// see https://github.com/FasterXML/jackson-databind/issues/230
@SuppressWarnings("unused")
@Getter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonDeserialize(using = JsonDeserializer.None.class)
public class Passport implements Serializable, CompositeElement {
	@JsonProperty(required = true)
	private String source;

	private String series;
	private String number;

	@JsonProperty(required = true)
	private QcPassport qc;


	@Override
	public CompositeElementType getCompositeElementType() {
		return CompositeElementType.PASSPORT;
	}
}
