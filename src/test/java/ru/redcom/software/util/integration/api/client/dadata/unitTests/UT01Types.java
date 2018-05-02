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

public class UT01Types {

/* TODO cleanup
	@Test
	public void fiasActuality() {
		assertThat(FiasActuality.jsonCreator(0), is(FiasActuality.ACTUAL));
		assertThat(FiasActuality.jsonCreator(1), is(FiasActuality.RENAMED));
		assertThat(FiasActuality.jsonCreator(50), is(FiasActuality.RENAMED));
		assertThat(FiasActuality.jsonCreator(51), is(FiasActuality.RESUBORDINATED));
		assertThat(FiasActuality.jsonCreator(99), is(FiasActuality.REMOVED));

		assertThat(FiasActuality.jsonCreator(100), is(FiasActuality.UNKNOWN));
	}
*/

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
		assertThat(QcAddressComplete.ADDRESS_COMPLETE.getPostalApplicability(), is(QcAddressComplete.PostalSuitability.YES));
		assertThat(QcAddressComplete.HOUSE_NOT_IN_FIAS.getPostalApplicability(), is(QcAddressComplete.PostalSuitability.MAYBE));
		assertThat(QcAddressComplete.NO_FLAT.getPostalApplicability(), is(QcAddressComplete.PostalSuitability.MAYBE));
		assertThat(QcAddressComplete.POSTAL_BOX.getPostalApplicability(), is(QcAddressComplete.PostalSuitability.MAYBE));
		assertThat(QcAddressComplete.VERIFY_PARSING.getPostalApplicability(), is(QcAddressComplete.PostalSuitability.MAYBE));
		assertThat(QcAddressComplete.NO_REGION.getPostalApplicability(), is(QcAddressComplete.PostalSuitability.NO));
		assertThat(QcAddressComplete.NO_CITY.getPostalApplicability(), is(QcAddressComplete.PostalSuitability.NO));
		assertThat(QcAddressComplete.NO_STREET.getPostalApplicability(), is(QcAddressComplete.PostalSuitability.NO));
		assertThat(QcAddressComplete.NO_HOUSE.getPostalApplicability(), is(QcAddressComplete.PostalSuitability.NO));
		assertThat(QcAddressComplete.ADDRESS_INCOMPLETE.getPostalApplicability(), is(QcAddressComplete.PostalSuitability.NO));
		assertThat(QcAddressComplete.FOREIGN_ADDRESS.getPostalApplicability(), is(QcAddressComplete.PostalSuitability.NO));
		assertThat(QcAddressComplete.UNKNOWN.getPostalApplicability(), is(QcAddressComplete.PostalSuitability.NO));
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
