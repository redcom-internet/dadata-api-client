/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.live;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClient;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClientException;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClientFactory;
import ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesError;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonLive.class)
@IfProfileValue(name = "live-tests", value = "enabled")
public class UT10Availability {

	@Autowired
	private DaDataClient dadata;

	@Test
	public void availableSilent() {
		final boolean available = dadata.checkAvailability(true);
		assertThat(available, is(true));
	}

	@Test
	public void notAvailableSilent() {
		final DaDataClient dadata = DaDataClientFactory.getInstance("0000000000000000000000000000000000000000", "0000000000000000000000000000000000000000");
		final boolean available = dadata.checkAvailability(true);
		assertThat(available, is(false));
	}

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void notAvailableException() {
		final TestCasesError.SampleErrorAddresses sample = TestCasesError.SampleErrorAddresses.CREDENTIALS_MISSING;
		exception.expect(DaDataClientException.class);
		exception.expectMessage(sample.getMessage());
		exception.expect(sample.getMatcher());
		final DaDataClient dadata = DaDataClientFactory.getInstance("0000000000000000000000000000000000000000", "0000000000000000000000000000000000000000");
		dadata.checkAvailability(false);
	}
}
