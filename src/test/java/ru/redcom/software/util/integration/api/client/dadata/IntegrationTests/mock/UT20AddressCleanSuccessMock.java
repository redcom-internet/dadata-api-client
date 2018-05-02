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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.redcom.software.util.integration.api.client.dadata.DaData;
import ru.redcom.software.util.integration.api.client.dadata.DaDataException;

import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCases.SampleAddress;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock.CommonMock.doTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest(DaData.class)
public class UT20AddressCleanSuccessMock {
	private static final String URI = "/clean/address";
	private static final HttpMethod METHOD = HttpMethod.POST;

	@Autowired
	private DaData dadata;
	@Autowired
	private MockRestServiceServer server;


	@Test
	public void cleanKhabarovsk1() throws DaDataException {
		test(SampleAddress.KHABAROVSK_1);
	}

	@Test
	public void cleanKhabarovsk2() throws DaDataException {
		test(SampleAddress.KHABAROVSK_2);
	}

	@Test
	public void cleanKhabarCityArea1() throws DaDataException {
		test(SampleAddress.KHABAR_CITYAREA_1);
	}

	@Test
	public void cleanKhabarCityArea2() throws DaDataException {
		test(SampleAddress.KHABAR_CITYAREA_2);
	}

	@Test
	public void cleanKhabarSettlement1() throws DaDataException {
		test(SampleAddress.KHABAR_SETTLEMENT_1);
	}

	@Test
	public void cleanKhabarSettlement2() throws DaDataException {
		test(SampleAddress.KHABAR_SETTLEMENT_2);
	}

	@Test
	public void cleanEao1() throws DaDataException {
		test(SampleAddress.EAO_1);
	}

	@Test
	public void cleanMoscow1() throws DaDataException {
		test(SampleAddress.MOSCOW_1);
	}

	@Test
	public void cleanMoscow2() throws DaDataException {
		test(SampleAddress.MOSCOW_2);
	}

	@Test
	public void cleanMoscowArea1() throws DaDataException {
		test(SampleAddress.MOSCOW_AREA_1);
	}

	@Test
	public void cleanMoscowArea2() throws DaDataException {
		test(SampleAddress.MOSCOW_AREA_2);
	}

	@Test
	public void cleanSpb1() throws DaDataException {
		test(SampleAddress.SPB_1);
	}

	@Test
	public void cleanNovosib1() throws DaDataException {
		test(SampleAddress.NOVOSIB_1);
	}

	@Test
	public void cleanPostbox1() throws DaDataException {
		test(SampleAddress.POSTBOX_1);
	}

	@Test
	public void cleanAmbigious1() throws DaDataException {
		test(SampleAddress.AMBIGIOUS_1);
	}

	@Test
	public void cleanIncomplete1() throws DaDataException {
		test(SampleAddress.INCOMPLETE_1);
	}

	@Test
	public void cleanForeign1() throws DaDataException {
		test(SampleAddress.FOREIGN_1);
	}

	@Test
	public void cleanUnparseable1() throws DaDataException {
		test(SampleAddress.UNPARSEABLE_1);
	}

	// shared test body
	private void test(final SampleAddress address) {
		doTest(dadata, server, URI, METHOD, address.getSourceAddress(), address.getJson(), address.getMatcher());
	}

	// TODO сделать один общий класс теста, и выбор реализации этого меотда в зависимости от профиля live/не-live?
}
