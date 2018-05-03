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
import ru.redcom.software.util.integration.api.client.dadata.DaDataException;
import ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.TestCasesError.SampleErrorAddresses;
import ru.redcom.software.util.integration.api.client.dadata.dto.APIErrorMessage;

import static org.hamcrest.Matchers.*;
import static ru.redcom.software.util.integration.api.client.dadata.APIErrorCode.BAD_REQUEST_FORMAT;
import static ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.mock.CommonMock.setupTestServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMock.class)
@RestClientTest
public class UT21AddressCleanErrorsMock {
	private static final String URI = "/clean/address";
	private static final HttpMethod METHOD = HttpMethod.POST;

	@Autowired
	private DaDataClient dadata;
	@Autowired
	private MockRestServiceServer server;


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
		test(SampleErrorAddresses.EMPTY_ARRAY);
	}

	// TODO implement other cases

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

	/*
		try {
			dadata.cleanAddresses((String) null);
		} catch (DaDataClientException e) {
			System.out.println("DaData client exception: " + e);
			System.out.println("HTTP status: " + e.getHttpStatusCode());
			System.out.println("HTTP status text: " + e.getHttpStatusText());
			System.out.println("API error code: " + e.getApiErrorCode());
			System.out.println("API error message: " + e.getApiErrorMessage());
			if (e.getApiErrorMessage() != null) {
				System.out.println("API error data: " + Arrays.toString(e.getApiErrorMessage().getData()));
				System.out.println("API error details: " + Arrays.toString(e.getApiErrorMessage().getDetails()));
				System.out.println("API error detail: " + e.getApiErrorMessage().getDetail());
				System.out.println("API error contents: " + e.getApiErrorMessage().getContents());
			}
		}
	*/
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

	// -----------------------------------------------------------------------------------------------------------------

	// shared test body
	private void test(final SampleErrorAddresses sample) {
		exception.expect(DaDataClientException.class);
		exception.expectMessage(sample.getMessage());
		exception.expect(sample.getMatcher());
		setupTestServer(server, URI, METHOD, sample.getRequestBody(), sample.getResponseStatus(), sample.getResponseBody());
		dadata.cleanAddresses((String[]) sample.getArgument());
	}

	// TODO тестирование ошибок формата на искусственном сервере
}
