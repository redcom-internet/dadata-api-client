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
import ru.redcom.software.util.integration.api.client.dadata.DaDataClientFactory;
import ru.redcom.software.util.integration.api.client.dadata.DaDataException;

import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessAddress.SampleAddresses;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessAddress.successTest;

/*
 * Sample query:
 * <pre>
 * 	curl -s -X POST \
 *     -H "Content-Type: application/json" \
 *     -H "Accept: application/json" \
 *     -H "Authorization: Token xxxxxxxxxxxxxxxxxxxxxxxx" \
 *     -H "X-Secret: xxxxxxxxxxxxxxxxxxx" \
 *     -d '[ "Хабаровский край, гор. Хабаровск, п. Берёзовка р-н, ул. Заводская, д.123, кв.111" ]' \
 *     https://dadata.ru/api/v2/clean/address | json_pp
 * </pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonLive.class)
@ActiveProfiles("live")
public class UT20AddressCleanSuccess {

	@Autowired
	private DaDataClient dadata;


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

	// Try alternative base uri (without ssl)
	@Test
	public void cleanUnparseable2() throws DaDataException {
		final SampleAddresses address = SampleAddresses.UNPARSEABLE_1;
		final DaDataClient dadata = DaDataClientFactory.getInstance(CommonLive.API_KEY, CommonLive.SECRET_KEY, "http://dadata.ru/api/v2");
		successTest(dadata, address.getSourcePattern(), address.getMatcher());
	}

	// -----------------------------------------------------------------------------------------------------------------

	// shared test body
	private void test(final SampleAddresses sample) {
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
	}
}
