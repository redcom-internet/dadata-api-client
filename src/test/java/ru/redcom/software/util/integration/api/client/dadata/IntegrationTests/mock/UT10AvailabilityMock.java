/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClient;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClientException;
import ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesError;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest
public class UT10AvailabilityMock {
	private static final String URI = "/status/CLEAN";
	private static final HttpMethod METHOD = HttpMethod.GET;

	@Autowired
	private DaDataClient dadata;
	@Autowired
	private MockRestServiceServer server;


	@Test
	public void availableSilent() {
		setupTestServer(server, URI, METHOD);
		final boolean available = dadata.checkAvailability(true);
		assertThat(available, is(true));
		server.verify();
	}

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void notAvailableSilent() {
		test(true);
	}

	@Test
	public void notAvailableException() {
		test(false);
	}

	private void test(final boolean silent) {
		final TestCasesError.SampleErrorAddresses sample = TestCasesError.SampleErrorAddresses.CREDENTIALS_MISSING;
		setupTestServer(server, URI, METHOD, null, sample.getResponseStatus(), sample.getResponseBody());
		if (!silent) {
			exception.expect(DaDataClientException.class);
			exception.expectMessage(sample.getMessage());
			exception.expect(sample.getMatcher());
		}
		final boolean available = dadata.checkAvailability(silent);
		assertThat(available, is(false));
		server.verify();
	}
}
