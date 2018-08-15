/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.unitTests;

import org.junit.Test;
import ru.redcom.lib.integration.api.client.dadata.types.QcBirthDate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UT06TypesBirthDate {

	@Test
	public void qcBirthDate() {
		assertThat(QcBirthDate.FULL.isManualVerificationRequired(), is(false));
		assertThat(QcBirthDate.PARTIAL.isManualVerificationRequired(), is(true));
		assertThat(QcBirthDate.UNRECOGNIZED.isManualVerificationRequired(), is(false));
		assertThat(QcBirthDate.UNKNOWN.isManualVerificationRequired(), is(false));
	}
}
