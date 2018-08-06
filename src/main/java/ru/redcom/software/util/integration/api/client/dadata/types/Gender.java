/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Person gender */
public enum Gender {
	@JsonProperty("М")
	MALE,
	@JsonProperty("Ж")
	FEMALE,
	/** Catch-all constant for unrecognized response contents */
	@JsonEnumDefaultValue
	UNKNOWN
}
