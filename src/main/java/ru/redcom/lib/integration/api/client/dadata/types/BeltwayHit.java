/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * Address position relative to beltway (ring road).
 *
 * @author boris
 */
public enum BeltwayHit {
	IN_MKAD,
	OUT_MKAD,
	IN_KAD,
	OUT_KAD,
	NONE,       // empty string deserializes as this member
	/** Catch-all constant for unrecognized response contents */
	@JsonEnumDefaultValue
	UNKNOWN;

	@SuppressWarnings("unused")
	@Nullable
	@JsonCreator
	private static BeltwayHit jsonCreator(final String s) {
		return s == null ? null : StringUtils.hasText(s) ? Arrays.stream(values()).filter(v -> v.name().equalsIgnoreCase(s)).findAny().orElse(UNKNOWN) : NONE;
	}
}
