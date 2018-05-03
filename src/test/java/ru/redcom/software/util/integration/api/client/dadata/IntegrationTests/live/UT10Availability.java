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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonLive.class)
@ActiveProfiles("live")
public class UT10Availability {

	@Autowired
	private DaDataClient dadata;

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
