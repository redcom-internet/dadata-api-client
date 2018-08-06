/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/*
Скопировать поле в ответ «как есть»

[
  {
    "source": "original string"
  }
]
*/

/**
 * Container for the string content passed through without modifications.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsIs extends ResponseItem {
	// This class is not instantiable
	private AsIs() {
	}
}
