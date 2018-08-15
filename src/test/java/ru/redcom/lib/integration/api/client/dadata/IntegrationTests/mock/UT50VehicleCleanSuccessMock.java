/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.IntegrationTests.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.DaDataException;

import static ru.redcom.lib.integration.api.client.dadata.IntegrationTests.TestCasesSuccessVehicle.SampleVehicles;
import static ru.redcom.lib.integration.api.client.dadata.IntegrationTests.TestCasesSuccessVehicle.successTest;
import static ru.redcom.lib.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest
public class UT50VehicleCleanSuccessMock {
	private static final String URI = "/clean/vehicle";
	private static final HttpMethod METHOD = HttpMethod.POST;

	@Autowired
	private DaDataClient dadata;
	@Autowired
	private MockRestServiceServer server;


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
		setupTestServer(server, URI, METHOD, sample.getRequestBody(), HttpStatus.OK, sample.getResponseBody());
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
		server.verify();
	}
}
