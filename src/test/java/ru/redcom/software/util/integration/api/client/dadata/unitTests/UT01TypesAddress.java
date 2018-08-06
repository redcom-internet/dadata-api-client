/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.unitTests;

import org.junit.Test;
import ru.redcom.software.util.integration.api.client.dadata.types.QcAddress;
import ru.redcom.software.util.integration.api.client.dadata.types.QcAddressComplete;
import ru.redcom.software.util.integration.api.client.dadata.types.QcHouse;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UT01TypesAddress {

	@Test
	public void qcAddress() {
		assertThat(QcAddress.FULL.isManualVerificationRequired(), is(false));
		assertThat(QcAddress.UNRECOGNIZED.isManualVerificationRequired(), is(false));
		assertThat(QcAddress.PARTIAL.isManualVerificationRequired(), is(true));
		assertThat(QcAddress.INVARIANTS.isManualVerificationRequired(), is(true));
		assertThat(QcAddress.UNKNOWN.isManualVerificationRequired(), is(false));
	}

	@Test
	public void qcAddressComplete() {
		assertThat(QcAddressComplete.ADDRESS_COMPLETE.getPostalSuitability(), is(QcAddressComplete.PostalSuitability.YES));
		assertThat(QcAddressComplete.HOUSE_NOT_IN_FIAS.getPostalSuitability(), is(QcAddressComplete.PostalSuitability.MAYBE));
		assertThat(QcAddressComplete.NO_FLAT.getPostalSuitability(), is(QcAddressComplete.PostalSuitability.MAYBE));
		assertThat(QcAddressComplete.POSTAL_BOX.getPostalSuitability(), is(QcAddressComplete.PostalSuitability.MAYBE));
		assertThat(QcAddressComplete.VERIFY_PARSING.getPostalSuitability(), is(QcAddressComplete.PostalSuitability.MAYBE));
		assertThat(QcAddressComplete.NO_REGION.getPostalSuitability(), is(QcAddressComplete.PostalSuitability.NO));
		assertThat(QcAddressComplete.NO_CITY.getPostalSuitability(), is(QcAddressComplete.PostalSuitability.NO));
		assertThat(QcAddressComplete.NO_STREET.getPostalSuitability(), is(QcAddressComplete.PostalSuitability.NO));
		assertThat(QcAddressComplete.NO_HOUSE.getPostalSuitability(), is(QcAddressComplete.PostalSuitability.NO));
		assertThat(QcAddressComplete.ADDRESS_INCOMPLETE.getPostalSuitability(), is(QcAddressComplete.PostalSuitability.NO));
		assertThat(QcAddressComplete.FOREIGN_ADDRESS.getPostalSuitability(), is(QcAddressComplete.PostalSuitability.NO));
		assertThat(QcAddressComplete.UNKNOWN.getPostalSuitability(), is(QcAddressComplete.PostalSuitability.NO));
	}

	@Test
	public void qcHouseMatchLevel() {
		assertThat(QcHouse.EXACT.getPostalProbability(), is(QcHouse.DeliveryProbability.HIGH));
		assertThat(QcHouse.SIMILAR.getPostalProbability(), is(QcHouse.DeliveryProbability.MODERATE));
		assertThat(QcHouse.RANGE.getPostalProbability(), is(QcHouse.DeliveryProbability.MODERATE));
		assertThat(QcHouse.NOT_FOUND.getPostalProbability(), is(QcHouse.DeliveryProbability.LOW));
		assertThat(QcHouse.UNKNOWN.getPostalProbability(), is(QcHouse.DeliveryProbability.LOW));
	}
}
