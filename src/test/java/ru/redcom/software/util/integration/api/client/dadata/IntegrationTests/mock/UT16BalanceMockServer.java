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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.redcom.software.util.integration.api.client.dadata.DaData;
import ru.redcom.software.util.integration.api.client.dadata.DaDataException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

// TODO выяснить, как правильно работать с этим режимом. Получается два объекта MockRestServiceServer.
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RestClientTest(DaData.class)
public class UT16BalanceMockServer {
	private static final String URI = "/profile/balance";
	private static final HttpMethod METHOD = HttpMethod.GET;
	private static final BigDecimal balanceValue = new BigDecimal("123456.78");

	@TestConfiguration
	static class RestTemplateBuilderProvider {
		@Bean
		public RestTemplateBuilder provideBuilder() {
			customizer = new MockServerRestTemplateCustomizer();
			return new RestTemplateBuilder(customizer);
		}
	}

	private static MockServerRestTemplateCustomizer customizer;

	@Autowired
	private DaData dadata;
	//	@Autowired
	//	private MockRestServiceServer server;
	// java.lang.IllegalStateException: Unable to use auto-configured MockRestServiceServer since MockServerRestTemplateCustomizer has not been bound to a RestTemplate
	//	@Autowired
//	private ObjectMapper objectMapper;


	@Before
	public void setup() {
		Map<RestTemplate, MockRestServiceServer> servers = customizer.getServers();
		for (Map.Entry<RestTemplate, MockRestServiceServer> s : servers.entrySet())
			System.out.println("server: key " + s.getKey() + ", value " + s.getValue());
		List<MockRestServiceServer> srv = new ArrayList<>(servers.values());
		final MockRestServiceServer server1 = srv.get(0);
		final MockRestServiceServer server2 = srv.get(1);

		final String responseBody = "{\"balance\":" + balanceValue + "}";
		setupTestServer(server1, URI, METHOD, responseBody);
	}

	@Test
	public void profileBalance() throws DaDataException {
		final BigDecimal balance = dadata.getProfileBalance();
		System.out.println("Balance = " + balance);
		assertThat(balance, equalTo(balanceValue));
	}
}
