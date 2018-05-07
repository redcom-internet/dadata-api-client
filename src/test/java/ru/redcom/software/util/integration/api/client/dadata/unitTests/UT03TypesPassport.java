/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.unitTests;

import org.junit.Test;
import ru.redcom.software.util.integration.api.client.dadata.types.QcPassport;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UT03TypesPassport {

	@Test
	public void qcPassport() {
		assertThat(QcPassport.VALID.isManualVerificationRequired(), is(false));
		assertThat(QcPassport.EMPTY.isManualVerificationRequired(), is(false));
		assertThat(QcPassport.BAD_FORMAT.isManualVerificationRequired(), is(true));
		assertThat(QcPassport.VOID.isManualVerificationRequired(), is(true));
	}
}
