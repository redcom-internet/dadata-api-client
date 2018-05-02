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
import ru.redcom.software.util.integration.api.client.dadata.DaData;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest(DaData.class)
public class UT10AvailabilityMock {
	private static final String URI = "/status/CLEAN";
	private static final HttpMethod METHOD = HttpMethod.GET;

	@Autowired
	private DaData dadata;
	@Autowired
	private MockRestServiceServer server;


	@Before
	public void setup() {
		setupTestServer(server, URI, METHOD);
	}

	@Test
	public void availableSilent() {
		final boolean available = dadata.checkAvailability(true);
		assertThat(available, is(true));
	}

	// TODO not available silent
	// TODO not available exception

/*
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void notAvailable() throws DaDataException {
		exception.expect(DaDataException.class);
		exception.expectMessage("Address cleaning service is not available");
		exception.expect(allOf(hasProperty("httpStatusCode", is(400)),
		                       hasProperty("httpStatusText", is("BAD REQUEST")),
		                       hasProperty("apiErrorCode", is(BAD_REQUEST_FORMAT)),
		                       hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		                       hasProperty("apiErrorMessage", hasProperty("detail", is("Bad request. Use non empty list."))),
		                       hasProperty("fatal", is(true))));
		// empty array
		dadata.cleanAddresses();
	}
*/

}
