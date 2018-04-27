/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UT10Availability {
	// TODO переделать на mocked json server objects и убрать обращения к настоящему DaData API с моими реквизитами
	private static final String apiKey = "d0ef3cc03e28295c6946d9f0b5240f844d0f91d0";
	private static final String secretKey = "73254dc8501cb6f5840a2a7a4c4d13ce49061985";

	private DaData dadata;

	@Before
	public void prepareApiBinding() {
		dadata = new DaData(apiKey, secretKey);
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
