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

import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessEmail.SampleEmails;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessEmail.successTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonLive.class)
@ActiveProfiles("live")
public class UT40EmailCleanSuccess {

	@Autowired
	private DaDataClient dadata;


	@Test
	public void cleanValid() throws DaDataException {
		test(SampleEmails.VALID);
	}

	@Test
	public void cleanInvalid() throws DaDataException {
		test(SampleEmails.INVALID);
	}

	@Test
	public void cleanInstant() throws DaDataException {
		test(SampleEmails.INSTANT);
	}

	@Test
	public void cleanCorrected() throws DaDataException {
		test(SampleEmails.CORRECTED);
	}

	@Test
	public void cleanUnrecognized() throws DaDataException {
		test(SampleEmails.UNRECOGNIZED);
	}

	@Test
	public void cleanEmpty() throws DaDataException {
		test(SampleEmails.EMPTY);
	}

	// -----------------------------------------------------------------------------------------------------------------

	// shared test body
	private void test(final SampleEmails sample) {
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
	}
}
