/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.live;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClient;
import ru.redcom.software.util.integration.api.client.dadata.DaDataException;

import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessName.SampleNames;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessName.successTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonLive.class)
@IfProfileValue(name = "live-tests", value = "enabled")
public class UT35NameCleanSuccess {

	@Autowired
	private DaDataClient dadata;


	@Test
	public void cleanMale1() throws DaDataException {
		test(SampleNames.MALE_1);
	}

	@Test
	public void cleanFemale1() throws DaDataException {
		test(SampleNames.FEMALE_1);
	}

	@Test
	public void cleanChinese() throws DaDataException {
		test(SampleNames.CHINESE);
	}

	@Test
	public void cleanSuspicious() throws DaDataException {
		test(SampleNames.SUSPICIOUS);
	}

	@Test
	public void cleanEmpty() throws DaDataException {
		test(SampleNames.EMPTY);
	}

	// -----------------------------------------------------------------------------------------------------------------

	// shared test body
	private void test(final SampleNames sample) {
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
	}
}
