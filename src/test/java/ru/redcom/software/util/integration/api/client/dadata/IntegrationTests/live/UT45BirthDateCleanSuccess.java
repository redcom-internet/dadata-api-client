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

import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessBirthDate.SampleBirthDates;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessBirthDate.successTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonLive.class)
@ActiveProfiles("live")
public class UT45BirthDateCleanSuccess {

	@Autowired
	private DaDataClient dadata;


	@Test
	public void cleanValid1() throws DaDataException {
		test(SampleBirthDates.VALID_1);
	}

	@Test
	public void cleanValid2() throws DaDataException {
		test(SampleBirthDates.VALID_2);
	}

	@Test
	public void cleanValid3() throws DaDataException {
		test(SampleBirthDates.VALID_3);
	}

	@Test
	public void cleanPartial() throws DaDataException {
		test(SampleBirthDates.PARTIAL);
	}

	@Test
	public void cleanEmpty() throws DaDataException {
		test(SampleBirthDates.EMPTY);
	}

	// -----------------------------------------------------------------------------------------------------------------

	// shared test body
	private void test(final SampleBirthDates sample) {
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
	}
}
