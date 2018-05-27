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
 * Marker interface to specify that element can participate in composite response.
 */
@SuppressWarnings("unused")
@Getter
@EqualsAndHashCode
@ToString
public abstract class ResponseItem implements Serializable {
	@JsonProperty(required = true)
	private String source;
}
