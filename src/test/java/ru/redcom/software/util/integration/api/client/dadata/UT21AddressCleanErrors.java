/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.redcom.software.util.integration.api.client.dadata.dto.APIErrorMessage;

import static org.hamcrest.Matchers.*;
import static ru.redcom.software.util.integration.api.client.dadata.APIErrorCode.BAD_REQUEST_FORMAT;

public class UT21AddressCleanErrors {
	// TODO переделать на mocked json server objects и убрать обращения к настоящему DaData API с моими реквизитами
	private static final String apiKey = "d0ef3cc03e28295c6946d9f0b5240f844d0f91d0";
	private static final String secretKey = "73254dc8501cb6f5840a2a7a4c4d13ce49061985";

	private DaData dadata;

	@Before
	public void prepareApiBinding() {
		dadata = new DaData(apiKey, secretKey);
	}


	@Test(expected = IllegalArgumentException.class)
	public void nullSingleAddress() throws DaDataException {
		//noinspection ConstantConditions
		dadata.cleanAddress(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptySingleAddress() throws DaDataException {
		dadata.cleanAddress("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullAddressArray() throws DaDataException {
		//noinspection ConstantConditions
		dadata.cleanAddresses((String[]) null);
	}

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void emptyAddressArray() throws DaDataException {
		exception.expect(DaDataClientException.class);
		exception.expectMessage("Incorrect request format or data type");
		exception.expect(allOf(hasProperty("httpStatusCode", is(400)),
		                       hasProperty("httpStatusText", is("BAD REQUEST")),
		                       hasProperty("apiErrorCode", is(BAD_REQUEST_FORMAT)),
		                       hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		                       hasProperty("apiErrorMessage", hasProperty("detail", is("Bad request. Use non empty list."))),
		                       hasProperty("fatal", is(true))));
		// empty array
		dadata.cleanAddresses();
	}

	@Test
	public void nullAddressArrayElement() throws DaDataException {
		exception.expect(DaDataClientException.class);
		exception.expectMessage("Incorrect request format or data type");
		exception.expect(allOf(hasProperty("httpStatusCode", is(400)),
		                       hasProperty("httpStatusText", is("BAD REQUEST")),
		                       hasProperty("apiErrorCode", is(BAD_REQUEST_FORMAT)),
		                       hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		                       hasProperty("apiErrorMessage", hasProperty("details", arrayContaining("Request does not contain data for standartization"))),
		                       hasProperty("fatal", is(true))));
//		try {
		// null string
		dadata.cleanAddresses((String) null);
//		} catch (DaDataClientException e) {
//			System.out.println("DaData client exception: " + e);
//			System.out.println("HTTP status: " + e.getHttpStatusCode());
//			System.out.println("HTTP status text: " + e.getHttpStatusText());
//			System.out.println("API error code: " + e.getApiErrorCode());
//			System.out.println("API error message: " + e.getApiErrorMessage());
//			if (e.getApiErrorMessage() != null) {
//				System.out.println("API error data: " + Arrays.toString(e.getApiErrorMessage().getData()));
//				System.out.println("API error details: " + Arrays.toString(e.getApiErrorMessage().getDetails()));
//				System.out.println("API error detail: " + e.getApiErrorMessage().getDetail());
//				System.out.println("API error contents: " + e.getApiErrorMessage().getContents());
//			}
//		}
		/*
		HTTP status: 400
		HTTP status text: BAD REQUEST
		API error code: BAD_REQUEST_FORMAT
		API error message: APIErrorMessage(contents={details=[Request does not contain data for standartization]})
		API error data: null
		API error details: [Request does not contain data for standartization]
		API error detail: null
		API error contents: {details=[Request does not contain data for standartization]}
		*/
	}

	@Test
	public void emptyAddressArrayElement() throws DaDataException {
		exception.expect(DaDataClientException.class);
		exception.expectMessage("Incorrect request format or data type");
		exception.expect(allOf(hasProperty("httpStatusCode", is(400)),
		                       hasProperty("httpStatusText", is("BAD REQUEST")),
		                       hasProperty("apiErrorCode", is(BAD_REQUEST_FORMAT)),
		                       hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		                       hasProperty("apiErrorMessage", hasProperty("details", arrayContaining("Request does not contain data for standartization"))),
		                       hasProperty("fatal", is(true))));
		// empty string
		dadata.cleanAddresses("");
	}

	// TODO тестирование ошибок формата на искусственном сервере
}
