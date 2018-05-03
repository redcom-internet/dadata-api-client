/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests;

import org.hamcrest.Matcher;
import org.springframework.http.HttpStatus;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClientException;
import ru.redcom.software.util.integration.api.client.dadata.dto.APIErrorMessage;

import java.util.Arrays;
import java.util.function.Supplier;

import static org.hamcrest.Matchers.*;
import static ru.redcom.software.util.integration.api.client.dadata.APIErrorCode.*;

public class TestCasesError {
	public enum SampleErrorAddresses {
		/*
				EMPTY_REQUEST_BODY(null,
							null,
							"Incorrect request format or data type",
							HttpStatus.BAD_REQUEST, "{\"data\":[\"Field is required\"]}",
							TestCasesError::matcherEmptyBody)
		*/
		EMPTY_ARRAY(new String[0],
		            "Incorrect request format or data type",
		            HttpStatus.BAD_REQUEST, "{\"detail\":\"Bad request. Use non empty list.\"}",
		            TestCasesError::matcherEmptyArray),
		EMPTY_ARRAY_ELEMENT(new String[] {""},
		                    "Incorrect request format or data type",
		                    HttpStatus.BAD_REQUEST, "{\"details\":[\"Request does not contain data for standartization\"]}",
		                    TestCasesError::matcherEmptyArrayElement),
		CREDENTIALS_MISSING(new String[] {"qwe"},
		                    "API key or Secret key are missing",
		                    HttpStatus.UNAUTHORIZED, "{\"detail\":\"Учетные данные не были предоставлены.\"}",
		                    TestCasesError::matcherCredentialsMissing),
		BALANCE_EXHAUSTED(new String[] {"qwe"},
		                  "Insufficient balance",
		                  HttpStatus.PAYMENT_REQUIRED, "{\"detail\":\"Zero balance\"}",
		                  TestCasesError::matcherBalanceExhausted),
		CREDENTIALS_INVALID(new String[] {"qwe"},
		                    "Access denied or API/Secret key is invalid",
		                    HttpStatus.FORBIDDEN, "{\"detail\":\"У вас нет прав для выполнения этой операции.\"}",
		                    TestCasesError::matcherCredentialsInvalid),
		UNSUPPORTED_METHOD(new String[] {"qwe"},
		                   "Unsupported request method",
		                   HttpStatus.NOT_ACCEPTABLE, "{\"detail\":\"Метод \\\"GET\\\" не разрешен.\"}",
		                   TestCasesError::matcherUnsuportedMethod),
		TOO_MANY_ITEMS(new String[] {"123", "456", "789", "qwe", "rty", "uio", "asd", "fgh", "jkl", "zxc", "vbn", "mp"},
		               "Too many items requested",
		               HttpStatus.PAYLOAD_TOO_LARGE, "{\"detail\":\"Too many items in request\"}",
		               TestCasesError::matcherTooManyItems),
		TOO_MANY_REQUESTS(new String[] {"qwe"},
		                  "Too many requests per second",
		                  HttpStatus.TOO_MANY_REQUESTS, "{\"detail\":\"Too many requests\"}",
		                  TestCasesError::matcherTooManyRequests);


		private final String[] argument;
		private final String message;
		private final HttpStatus responseStatus;
		private final String responseBody;
		private final Supplier<Matcher<DaDataClientException>> matcher;

		SampleErrorAddresses(final String[] argument, final String message,
		                     final HttpStatus responseStatus, final String responseBody,
		                     final Supplier<Matcher<DaDataClientException>> matcher) {
			this.argument = argument;
			this.message = message;
			this.responseStatus = responseStatus;
			this.responseBody = responseBody;
			this.matcher = matcher;
		}

		public Object getArgument() {
			return argument;
		}

		public String getRequestBody() {
			return Arrays.toString(Arrays.stream(argument).map((s) -> "\"" + s.replace("\"", "\\\"") + "\"").toArray());
		}

		public String getMessage() {
			return message;
		}

		public HttpStatus getResponseStatus() {
			return responseStatus;
		}

		public String getResponseBody() {
			return responseBody;
		}

		public Matcher<DaDataClientException> getMatcher() {
			return matcher != null ? matcher.get() : nullValue(DaDataClientException.class);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	/*
		// empty request body
		private static Matcher<DaDataClientException> matcherEmptyBody() {
			return allOf(hasProperty("httpStatusCode", is(400)),
						 hasProperty("httpStatusText", is(equalToIgnoringCase("BAD REQUEST"))),
						 hasProperty("apiErrorCode", is(BAD_REQUEST_FORMAT)),
						 hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
						 hasProperty("apiErrorMessage", hasProperty("data", is("Field is required"))),
						 hasProperty("fatal", is(true)));
		}
	*/

	// empty address array
	private static Matcher<DaDataClientException> matcherEmptyArray() {
		return allOf(hasProperty("httpStatusCode", is(400)),
		             hasProperty("httpStatusText", is(equalToIgnoringCase("BAD REQUEST"))),
		             hasProperty("apiErrorCode", is(BAD_REQUEST_FORMAT)),
		             hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		             hasProperty("apiErrorMessage", hasProperty("detail", is("Bad request. Use non empty list."))),
		             hasProperty("fatal", is(true)));
	}

	// empty address array element
	private static Matcher<DaDataClientException> matcherEmptyArrayElement() {
		return allOf(hasProperty("httpStatusCode", is(400)),
		             hasProperty("httpStatusText", is(equalToIgnoringCase("BAD REQUEST"))),
		             hasProperty("apiErrorCode", is(BAD_REQUEST_FORMAT)),
		             hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		             hasProperty("apiErrorMessage", hasProperty("details", is(arrayContaining("Request does not contain data for standartization")))),
		             hasProperty("fatal", is(true)));
	}

	// credentials missing
	private static Matcher<DaDataClientException> matcherCredentialsMissing() {
		return allOf(hasProperty("httpStatusCode", is(401)),
		             hasProperty("httpStatusText", is(equalToIgnoringCase("UNAUTHORIZED"))),
		             hasProperty("apiErrorCode", is(CREDENTIALS_MISSING)),
		             hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		             hasProperty("apiErrorMessage", hasProperty("detail", is("Учетные данные не были предоставлены."))),
		             hasProperty("fatal", is(true)));
	}

	// balance exhausted
	private static Matcher<DaDataClientException> matcherBalanceExhausted() {
		return allOf(hasProperty("httpStatusCode", is(402)),
		             hasProperty("httpStatusText", is(equalToIgnoringCase("PAYMENT REQUIRED"))),
		             hasProperty("apiErrorCode", is(INSUFFICIENT_BALANCE)),
		             hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		             hasProperty("apiErrorMessage", hasProperty("detail", is("Zero balance"))),
		             hasProperty("fatal", is(false)));
	}

	// credentials invalid
	private static Matcher<DaDataClientException> matcherCredentialsInvalid() {
		return allOf(hasProperty("httpStatusCode", is(403)),
		             hasProperty("httpStatusText", is(equalToIgnoringCase("FORBIDDEN"))),
		             hasProperty("apiErrorCode", is(CREDENTIALS_INVALID)),
		             hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		             hasProperty("apiErrorMessage", hasProperty("detail", is("У вас нет прав для выполнения этой операции."))),
		             hasProperty("fatal", is(true)));
	}

	// unsupported method
	private static Matcher<DaDataClientException> matcherUnsuportedMethod() {
		return allOf(hasProperty("httpStatusCode", is(406)),
		             hasProperty("httpStatusText", is(equalToIgnoringCase("NOT ACCEPTABLE"))),
		             hasProperty("apiErrorCode", is(UNSUPPORTED_METHOD)),
		             hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		             hasProperty("apiErrorMessage", hasProperty("detail", is("Метод \"GET\" не разрешен."))),
		             hasProperty("fatal", is(true)));
	}

	// too many items
	private static Matcher<DaDataClientException> matcherTooManyItems() {
		return allOf(hasProperty("httpStatusCode", is(413)),
		             hasProperty("httpStatusText", is(equalToIgnoringCase("PAYLOAD TOO LARGE"))),
		             hasProperty("apiErrorCode", is(TOO_MANY_ITEMS)),
		             hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		             hasProperty("apiErrorMessage", hasProperty("detail", is("Too many items in request"))),
		             hasProperty("fatal", is(false)));
	}

	// too many requests
	private static Matcher<DaDataClientException> matcherTooManyRequests() {
		return allOf(hasProperty("httpStatusCode", is(429)),
		             hasProperty("httpStatusText", is(equalToIgnoringCase("TOO MANY REQUESTS"))),
		             hasProperty("apiErrorCode", is(TOO_MANY_REQUESTS)),
		             hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		             hasProperty("apiErrorMessage", hasProperty("detail", is("Too many requests"))),
		             hasProperty("fatal", is(false)));
	}

	// =================================================================================================================
}
