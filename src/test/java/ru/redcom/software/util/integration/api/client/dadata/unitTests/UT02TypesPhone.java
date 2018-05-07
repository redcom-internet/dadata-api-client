/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.unitTests;

import org.junit.Test;
import ru.redcom.software.util.integration.api.client.dadata.types.QcPhone;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UT02TypesPhone {

	@Test
	public void qcPhone() {
		assertThat(QcPhone.FULL.isManualVerificationRequired(), is(false));
		assertThat(QcPhone.UNRECOGNIZED.isManualVerificationRequired(), is(false));
		assertThat(QcPhone.PARTIAL.isManualVerificationRequired(), is(true));
		assertThat(QcPhone.INVARIANTS.isManualVerificationRequired(), is(true));
		assertThat(QcPhone.UNKNOWN.isManualVerificationRequired(), is(false));
	}
}
