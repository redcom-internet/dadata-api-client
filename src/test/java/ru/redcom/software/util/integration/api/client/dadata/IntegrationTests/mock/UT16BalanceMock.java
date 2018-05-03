/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClient;
import ru.redcom.software.util.integration.api.client.dadata.DaDataException;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest
public class UT16BalanceMock {
	private static final String URI = "/profile/balance";
	private static final HttpMethod METHOD = HttpMethod.GET;
	private static final BigDecimal balanceValue = new BigDecimal("123456.78");

	@Autowired
	private DaDataClient dadata;
	@Autowired
	private MockRestServiceServer server;


	@Before
	public void setup() {
		final String responseBody = "{\"balance\":" + balanceValue + "}";
		setupTestServer(server, URI, METHOD, responseBody);
	}

	@Test
	public void profileBalance() throws DaDataException {
		final BigDecimal balance = dadata.getProfileBalance();
		System.out.println("Balance = " + balance);
		assertThat(balance, equalTo(balanceValue));
		server.verify();
	}
}
