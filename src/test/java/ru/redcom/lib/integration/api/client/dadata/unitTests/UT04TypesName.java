/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.unitTests;

import org.junit.Test;
import ru.redcom.lib.integration.api.client.dadata.types.QcName;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UT04TypesName {

	@Test
	public void qcName() {
		assertThat(QcName.FULL.isManualVerificationRequired(), is(false));
		assertThat(QcName.UNRECOGNIZED.isManualVerificationRequired(), is(false));
		assertThat(QcName.PARTIAL.isManualVerificationRequired(), is(true));
		assertThat(QcName.UNKNOWN.isManualVerificationRequired(), is(false));
	}
}
