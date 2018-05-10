/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.unitTests;

import org.junit.Test;
import ru.redcom.software.util.integration.api.client.dadata.types.QcVehicle;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UT07TypesVehicle {

	@Test
	public void qcVehicle() {
		assertThat(QcVehicle.FULL.isManualVerificationRequired(), is(false));
		assertThat(QcVehicle.PARTIAL.isManualVerificationRequired(), is(true));
		assertThat(QcVehicle.UNRECOGNIZED.isManualVerificationRequired(), is(false));
		assertThat(QcVehicle.UNKNOWN.isManualVerificationRequired(), is(false));
	}
}
