/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum BeltwayHit {
	IN_MKAD,
	OUT_MKAD,
	IN_KAD,
	OUT_KAD,
	@JsonProperty   // TODO check empty string deserializes as this member
			NONE,
	@JsonEnumDefaultValue
	UNKNOWN
}
