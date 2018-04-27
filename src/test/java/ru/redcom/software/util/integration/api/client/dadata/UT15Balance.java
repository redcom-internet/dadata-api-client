/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class UT15Balance {
	// TODO переделать на mocked json server objects и убрать обращения к настоящему DaData API с моими реквизитами
	private static final String apiKey = "d0ef3cc03e28295c6946d9f0b5240f844d0f91d0";
	private static final String secretKey = "73254dc8501cb6f5840a2a7a4c4d13ce49061985";

	private DaData dadata;

	@Before
	public void prepareApiBinding() {
		dadata = new DaData(apiKey, secretKey);
	}

	@Test
	public void profileBalance() throws DaDataException {
		final BigDecimal balance = dadata.getProfileBalance();
		System.out.println("Balance = " + balance);
		assertThat(balance, allOf(is(greaterThanOrEqualTo(BigDecimal.valueOf(0L))), is(lessThanOrEqualTo(BigDecimal.valueOf(100L)))));
	}
}
