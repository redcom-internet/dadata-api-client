/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;

/*
{
   "balance" : 0.2
}
 */

/**
 * Data Transfer Object for account profile balance response.
 *
 * @author boris
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance implements Serializable {
	@NonNull private final BigDecimal balance;

	@JsonCreator
	private Balance(@JsonProperty(value = "balance", required = true) @NonNull final BigDecimal balance) {
		this.balance = balance;
	}
}
