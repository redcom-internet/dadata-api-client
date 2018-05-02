/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import java.util.Arrays;

public enum BeltwayHit {
	IN_MKAD,
	OUT_MKAD,
	IN_KAD,
	OUT_KAD,
	NONE,       // empty string deserializes as this member
	@JsonEnumDefaultValue
	UNKNOWN;

	@SuppressWarnings("unused")
	@JsonCreator
	@Nullable
	private static BeltwayHit jsonCreator(final String s) {
		return s == null ? null : StringUtils.hasText(s) ? Arrays.stream(values()).filter(v -> v.name().equalsIgnoreCase(s)).findAny().orElse(UNKNOWN) : NONE;
	}
}
