/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.unitTests;

import org.junit.Test;
import ru.redcom.software.util.integration.api.client.dadata.types.QcEmail;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UT05TypesEmail {

	@Test
	public void qcName() {
		assertThat(QcEmail.VALID.isManualVerificationRequired(), is(false));
		assertThat(QcEmail.INVALID.isManualVerificationRequired(), is(true));
		assertThat(QcEmail.UNRECOGNIZED.isManualVerificationRequired(), is(false));
		assertThat(QcEmail.INSTANT.isManualVerificationRequired(), is(false));
		assertThat(QcEmail.CORRECTED.isManualVerificationRequired(), is(true));
		assertThat(QcEmail.UNKNOWN.isManualVerificationRequired(), is(false));
	}
}
