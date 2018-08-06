/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.math.BigDecimal;

/*
{
   "balance" : 0.2
}
 */

/**
 * Data Transfer Object for account profile balance response.
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance implements Serializable {
	@Nonnull private final BigDecimal balance;

	@JsonCreator
	private Balance(@JsonProperty(value = "balance", required = true) @Nonnull final BigDecimal balance) {
		this.balance = balance;
	}
}
