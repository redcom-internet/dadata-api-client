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
import ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessComposite;

import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessComposite.successTest;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest
public class UT55CompositeCleanSuccessMock {

	private static final String URI = "/clean";
	private static final HttpMethod METHOD = HttpMethod.POST;

	@Autowired
	private DaDataClient dadata;
	@Autowired
	private MockRestServiceServer server;

	@Test
	public void cleanSamplesFull() throws DaDataException {
		test(TestCasesSuccessComposite.SampleComposite.SAMPLES_FULL);
	}

	@Test
	public void cleanSamplesGaps() throws DaDataException {
		test(TestCasesSuccessComposite.SampleComposite.SAMPLES_GAPS);
	}

	// -----------------------------------------------------------------------------------------------------------------

	// shared test body
	private void test(final TestCasesSuccessComposite.SampleComposite sample) {
		setupTestServer(server, URI, METHOD, sample.getRequestBody(), HttpStatus.OK, sample.getResponseBody());
		successTest(dadata, sample.getRequest(), sample.getResponseMatcher());
		server.verify();
	}
}
