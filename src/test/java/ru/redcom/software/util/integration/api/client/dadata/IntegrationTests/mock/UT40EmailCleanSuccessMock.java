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

import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessEmail.SampleEmails;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessEmail.successTest;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest
public class UT40EmailCleanSuccessMock {
	private static final String URI = "/clean/email";
	private static final HttpMethod METHOD = HttpMethod.POST;

	@Autowired
	private DaDataClient dadata;
	@Autowired
	private MockRestServiceServer server;


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
		setupTestServer(server, URI, METHOD, sample.getRequestBody(), HttpStatus.OK, sample.getResponseBody());
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
		server.verify();
	}
}
