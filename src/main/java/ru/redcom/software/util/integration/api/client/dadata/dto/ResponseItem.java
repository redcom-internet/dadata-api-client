/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Base class for all response DTO types.
 *
 * @author boris
 */
@SuppressWarnings("unused")
@Getter
@EqualsAndHashCode
@ToString
public abstract class ResponseItem implements Serializable {
	// Property annotation with "required" does not currently enforce mandatoriness for fields on deserialization.
	// see https://github.com/FasterXML/jackson-databind/issues/230
	@JsonProperty(required = true)
	private String source;
}
