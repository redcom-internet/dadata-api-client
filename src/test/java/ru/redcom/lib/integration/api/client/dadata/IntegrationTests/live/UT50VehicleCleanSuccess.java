/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.IntegrationTests.live;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;
import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.DaDataException;

import static ru.redcom.lib.integration.api.client.dadata.IntegrationTests.TestCasesSuccessVehicle.SampleVehicles;
import static ru.redcom.lib.integration.api.client.dadata.IntegrationTests.TestCasesSuccessVehicle.successTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonLive.class)
@IfProfileValue(name = "live-tests", value = "enabled")
public class UT50VehicleCleanSuccess {

	@Autowired
	private DaDataClient dadata;


	@Test
	public void cleanValid1() throws DaDataException {
		test(SampleVehicles.VALID_1);
	}

	@Test
	public void cleanValid2() throws DaDataException {
		test(SampleVehicles.VALID_2);
	}

	@Test
	public void cleanValid3() throws DaDataException {
		test(SampleVehicles.VALID_3);
	}

	@Test
	public void cleanPartial() throws DaDataException {
		test(SampleVehicles.PARTIAL);
	}

	@Test
	public void cleanEmpty() throws DaDataException {
		test(SampleVehicles.EMPTY);
	}

	// -----------------------------------------------------------------------------------------------------------------

	// shared test body
	private void test(final SampleVehicles sample) {
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
	}
}
