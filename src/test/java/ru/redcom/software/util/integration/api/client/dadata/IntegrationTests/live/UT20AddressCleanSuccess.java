/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.live;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClient;
import ru.redcom.software.util.integration.api.client.dadata.DaDataException;
import ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccess;

import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccess.successTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonLive.class)
@ActiveProfiles("live")
public class UT20AddressCleanSuccess {

	@Autowired
	private DaDataClient dadata;


	@Test
	public void cleanKhabarovsk1() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.KHABAROVSK_1);
	}

	@Test
	public void cleanKhabarovsk2() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.KHABAROVSK_2);
	}

	@Test
	public void cleanKhabarCityArea1() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.KHABAR_CITYAREA_1);
	}

	@Test
	public void cleanKhabarCityArea2() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.KHABAR_CITYAREA_2);
	}

	@Test
	public void cleanKhabarSettlement1() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.KHABAR_SETTLEMENT_1);
	}

	@Test
	public void cleanKhabarSettlement2() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.KHABAR_SETTLEMENT_2);
	}

	@Test
	public void cleanEao1() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.EAO_1);
	}

	@Test
	public void cleanMoscow1() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.MOSCOW_1);
	}

	@Test
	public void cleanMoscow2() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.MOSCOW_2);
	}

	@Test
	public void cleanMoscowArea1() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.MOSCOW_AREA_1);
	}

	@Test
	public void cleanMoscowArea2() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.MOSCOW_AREA_2);
	}

	@Test
	public void cleanSpb1() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.SPB_1);
	}

	@Test
	public void cleanNovosib1() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.NOVOSIB_1);
	}

	@Test
	public void cleanPostbox1() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.POSTBOX_1);
	}

	@Test
	public void cleanAmbigious1() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.AMBIGIOUS_1);
	}

	@Test
	public void cleanIncomplete1() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.INCOMPLETE_1);
	}

	@Test
	public void cleanForeign1() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.FOREIGN_1);
	}

	@Test
	public void cleanUnparseable1() throws DaDataException {
		test(TestCasesSuccess.SampleAddresses.UNPARSEABLE_1);
	}


	// shared test body
	private void test(final TestCasesSuccess.SampleAddresses address) {
		successTest(dadata, address.getSourceAddress(), address.getMatcher());
	}

	// TODO доделать остальные тесты
}
