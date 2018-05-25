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

import java.io.Serializable;

/*
Скопировать поле в ответ «как есть»

[
  {
    "source": "original string"
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
public class AsIs implements Serializable, CompositeElement {
	@JsonProperty(required = true)
	private String source;


	@Override
	public CompositeElementType getCompositeElementType() {
		return CompositeElementType.AS_IS;
	}
}
