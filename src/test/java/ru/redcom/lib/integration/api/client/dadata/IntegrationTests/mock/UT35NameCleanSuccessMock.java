/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.IntegrationTests.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.DaDataException;

import static ru.redcom.lib.integration.api.client.dadata.IntegrationTests.TestCasesSuccessName.SampleNames;
import static ru.redcom.lib.integration.api.client.dadata.IntegrationTests.TestCasesSuccessName.successTest;
import static ru.redcom.lib.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest
public class UT35NameCleanSuccessMock {
	private static final String URI = "/clean/name";
	private static final HttpMethod METHOD = HttpMethod.POST;

	@Autowired
	private DaDataClient dadata;
	@Autowired
	private MockRestServiceServer server;


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
		setupTestServer(server, URI, METHOD, sample.getRequestBody(), HttpStatus.OK, sample.getResponseBody());
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
		server.verify();
	}
}
