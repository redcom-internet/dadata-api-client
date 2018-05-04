/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClient;
import ru.redcom.software.util.integration.api.client.dadata.DaDataException;

import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessAddress.SampleAddresses;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessAddress.successTest;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest
public class UT20AddressCleanSuccessMock {
	private static final String URI = "/clean/address";
	private static final HttpMethod METHOD = HttpMethod.POST;

	@Autowired
	private DaDataClient dadata;
	@Autowired
	private MockRestServiceServer server;


	@Test
	public void cleanKhabarovsk1() throws DaDataException {
		test(SampleAddresses.KHABAROVSK_1);
	}

	@Test
	public void cleanKhabarovsk2() throws DaDataException {
		test(SampleAddresses.KHABAROVSK_2);
	}

	@Test
	public void cleanKhabarCityArea1() throws DaDataException {
		test(SampleAddresses.KHABAR_CITYAREA_1);
	}

	@Test
	public void cleanKhabarCityArea2() throws DaDataException {
		test(SampleAddresses.KHABAR_CITYAREA_2);
	}

	@Test
	public void cleanKhabarSettlement1() throws DaDataException {
		test(SampleAddresses.KHABAR_SETTLEMENT_1);
	}

	@Test
	public void cleanKhabarSettlement2() throws DaDataException {
		test(SampleAddresses.KHABAR_SETTLEMENT_2);
	}

	@Test
	public void cleanEao1() throws DaDataException {
		test(SampleAddresses.EAO_1);
	}

	@Test
	public void cleanMoscow1() throws DaDataException {
		test(SampleAddresses.MOSCOW_1);
	}

	@Test
	public void cleanMoscow2() throws DaDataException {
		test(SampleAddresses.MOSCOW_2);
	}

	@Test
	public void cleanMoscowArea1() throws DaDataException {
		test(SampleAddresses.MOSCOW_AREA_1);
	}

	@Test
	public void cleanMoscowArea2() throws DaDataException {
		test(SampleAddresses.MOSCOW_AREA_2);
	}

	@Test
	public void cleanSpb1() throws DaDataException {
		test(SampleAddresses.SPB_1);
	}

	@Test
	public void cleanNovosib1() throws DaDataException {
		test(SampleAddresses.NOVOSIB_1);
	}

	@Test
	public void cleanPostbox1() throws DaDataException {
		test(SampleAddresses.POSTBOX_1);
	}

	@Test
	public void cleanAmbigious1() throws DaDataException {
		test(SampleAddresses.AMBIGIOUS_1);
	}

	@Test
	public void cleanIncomplete1() throws DaDataException {
		test(SampleAddresses.INCOMPLETE_1);
	}

	@Test
	public void cleanForeign1() throws DaDataException {
		test(SampleAddresses.FOREIGN_1);
	}

	@Test
	public void cleanUnparseable1() throws DaDataException {
		test(SampleAddresses.UNPARSEABLE_1);
	}

	// shared test body
	private void test(final SampleAddresses sample) {
		setupTestServer(server, URI, METHOD, sample.getRequestBody(), HttpStatus.OK, sample.getResponseBody());
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
		server.verify();
	}
}
