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

import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessPassport.SamplePassports;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessPassport.successTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonLive.class)
@ActiveProfiles("live")
public class UT30PassportCleanSuccess {

	@Autowired
	private DaDataClient dadata;


	@Test
	public void cleanValid1() throws DaDataException {
		test(SamplePassports.VALID_1);
	}

	@Test
	public void cleanVoid1() throws DaDataException {
		test(SamplePassports.VOID_1);
	}

	@Test
	public void cleanBadFormat1() throws DaDataException {
		test(SamplePassports.BAD_FORMAT_1);
	}

	@Test
	public void cleanEmpty1() throws DaDataException {
		test(SamplePassports.EMPTY_1);
	}

	// -----------------------------------------------------------------------------------------------------------------

	// shared test body
	private void test(final SamplePassports sample) {
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
	}
}
