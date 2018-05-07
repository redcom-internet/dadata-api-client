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

import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessPassport.SamplePassports;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesSuccessPassport.successTest;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest
public class UT30PassportCleanSuccessMock {
	private static final String URI = "/clean/passport";
	private static final HttpMethod METHOD = HttpMethod.POST;

	@Autowired
	private DaDataClient dadata;
	@Autowired
	private MockRestServiceServer server;


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
		setupTestServer(server, URI, METHOD, sample.getRequestBody(), HttpStatus.OK, sample.getResponseBody());
		successTest(dadata, sample.getSourcePattern(), sample.getMatcher());
		server.verify();
	}
}
