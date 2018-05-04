/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests;

import org.hamcrest.Matcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;
import ru.redcom.software.util.integration.api.client.dadata.APIErrorCode;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClientException;
import ru.redcom.software.util.integration.api.client.dadata.DaDataException;
import ru.redcom.software.util.integration.api.client.dadata.dto.APIErrorMessage;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static ru.redcom.software.util.integration.api.client.dadata.APIErrorCode.*;

public class TestCasesError {
	public enum SampleErrorAddresses {
		/*
				EMPTY_REQUEST_BODY(null,
							null,
							"Incorrect request format or data type",
							HttpStatus.BAD_REQUEST, "{\"data\":[\"Field is required\"]}",
			                DaDataClientException.class,
							TestCasesError::matcherEmptyBody)
		*/
		EMPTY_ARRAY(new String[0],
		            "Incorrect request format or data type",
		            HttpStatus.BAD_REQUEST, "{\"detail\":\"Bad request. Use non empty list.\"}",
		            DaDataClientException.class,
		            TestCasesError::matcherEmptyArray),
		EMPTY_ARRAY_ELEMENT(new String[] {""},
		                    "Incorrect request format or data type",
		                    HttpStatus.BAD_REQUEST, "{\"details\":[\"Request does not contain data for standartization\"]}",
		                    DaDataClientException.class,
		                    TestCasesError::matcherEmptyArrayElement),
		NULL_ARRAY_ELEMENT(new String[] {null},
		                   "Incorrect request format or data type",
		                   HttpStatus.BAD_REQUEST, "{\"details\":[\"Request does not contain data for standartization\"]}",
		                   DaDataClientException.class,
		                   TestCasesError::matcherEmptyArrayElement),
		CREDENTIALS_BAD_FORMAT(new String[] {"qwe"},
		                       "API key or Secret key are missing",
		                       HttpStatus.UNAUTHORIZED, "{\"detail\":\"Format of the given token seems invalid. Please, verify it in the profile page.\"}",
		                       DaDataClientException.class,
		                       TestCasesError::matcherCredentialsBadFormat),
		CREDENTIALS_MISSING(new String[] {"qwe"},
		                    "API key or Secret key are missing",
		                    HttpStatus.UNAUTHORIZED, "{\"detail\":\"Учетные данные не были предоставлены.\"}",
		                    DaDataClientException.class,
		                    TestCasesError::matcherCredentialsMissing),
		BALANCE_EXHAUSTED(new String[] {"qwe"},
		                  "Insufficient balance",
		                  HttpStatus.PAYMENT_REQUIRED, "{\"detail\":\"Zero balance\"}",
		                  DaDataClientException.class,
		                  TestCasesError::matcherBalanceExhausted),
		CREDENTIALS_INVALID(new String[] {"qwe"},
		                    "Access denied or API/Secret key is invalid",
		                    HttpStatus.FORBIDDEN, "{\"detail\":\"У вас нет прав для выполнения этой операции.\"}",
		                    DaDataClientException.class,
		                    TestCasesError::matcherCredentialsInvalid),
		UNSUPPORTED_METHOD(new String[] {"qwe"},
		                   "Unsupported request method",
		                   HttpStatus.NOT_ACCEPTABLE, "{\"detail\":\"Метод \\\"GET\\\" не разрешен.\"}",
		                   DaDataClientException.class,
		                   TestCasesError::matcherUnsuportedMethod),
		TOO_MANY_ITEMS(Stream.generate(() -> "test address").limit(100).toArray(String[]::new),
		               "Too many items requested",
		               HttpStatus.PAYLOAD_TOO_LARGE, "{\"detail\":\"Too many items in request\"}",
		               DaDataClientException.class,
		               TestCasesError::matcherTooManyItems),
		/*
				TOO_MANY_ITEMS1(Stream.generate(() -> "test address").limit(100).toArray(String[]::new),
								"Incorrect request format or data type",
								HttpStatus.PAYLOAD_TOO_LARGE, "{\"details\":\"Processing failed. Document should not contain more than 50 records.\"}",
			                    DaDataClientException.class,
								TestCasesError::matcherTooManyItems1),
		*/
		TOO_MANY_REQUESTS(new String[] {"Россия, Санкт-Петербург, ул.Восстания, д.1"},
		                  "Too many requests per second",
		                  HttpStatus.TOO_MANY_REQUESTS, "{\"detail\":\"Too many requests\"}",
		                  DaDataClientException.class,
		                  TestCasesError::matcherTooManyRequests),
		EMPTY_RESPONSE1(new String[] {"qwe"},
		                "Empty response from REST service",
		                HttpStatus.OK, null,
		                DaDataException.class,
		                TestCasesError::matcherEmptyResponse1),
		EMPTY_RESPONSE2(new String[] {"qwe"},
		                "Empty response from REST service",
		                HttpStatus.OK, "",
		                DaDataException.class,
		                TestCasesError::matcherEmptyResponse1),
		EMPTY_RESPONSE3(new String[] {"qwe"},
		                "Empty response from DaData API",
		                HttpStatus.OK, "[]",
		                DaDataException.class,
		                TestCasesError::matcherEmptyResponse1),
		EMPTY_RESPONSE4(new String[] {"qwe"},
		                "REST resource access or message format error",
		                HttpStatus.OK, "{}",
		                DaDataException.class,
		                TestCasesError::matcherEmptyResponse4),
		EMPTY_RESPONSE5(new String[] {"qwe"},
		                "REST resource access or message format error",
		                HttpStatus.OK, "[\"\"]",
		                DaDataException.class,
		                TestCasesError::matcherEmptyResponse5),
		WRONG_RESPONSE1(new String[] {"qwe"},
		                "REST resource access or message format error",
		                HttpStatus.OK, "[\"\"]",
		                DaDataException.class,
		                TestCasesError::matcherEmptyResponse5),
		WRONG_RESPONSE2(new String[] {"qwe"},
		                "REST service response code is not success (204)",
		                HttpStatus.NO_CONTENT, "[]",
		                DaDataException.class,
		                TestCasesError::matcherEmptyResponse1),
		INTERNAL_SERVER_ERROR_RESPONSE(new String[] {"qwe"},
		                               "HTTP Server error",
		                               HttpStatus.INTERNAL_SERVER_ERROR, "{\"detail\":\"Shit happens\"}",
		                               DaDataClientException.class,
		                               TestCasesError::matcherInternalServerError),
		WRONG_ERROR_RESPONSE(new String[0],
		                     "Incorrect request format or data type",
		                     HttpStatus.BAD_REQUEST, "{",
		                     DaDataClientException.class,
		                     TestCasesError::matcherWrongErrorResponse);


		private final String[] argument;
		private final String message;
		private final HttpStatus responseStatus;
		private final String responseBody;
		private final Class<? extends DaDataException> exceptionClass;
		private final Supplier<Matcher<? extends DaDataException>> matcher;

		SampleErrorAddresses(final String[] argument, final String message,
		                     final HttpStatus responseStatus, final String responseBody,
		                     final Class<? extends DaDataException> exceptionClass,
		                     final Supplier<Matcher<? extends DaDataException>> matcher) {
			this.argument = argument;
			this.message = message;
			this.responseStatus = responseStatus;
			this.responseBody = responseBody;
			this.exceptionClass = exceptionClass;
			this.matcher = matcher;
		}

		public String[] getArgument() {
			return argument;
		}

		public String getRequestBody() {
			return Arrays.toString(Arrays.stream(argument).map((s) -> s == null ? null : "\"" + s.replace("\"", "\\\"") + "\"").toArray());
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

		public Class<? extends DaDataException> getExceptionClass() {
			return exceptionClass;
		}

		public Matcher<? extends DaDataException> getMatcher() {
			return matcher != null ? matcher.get() : nullValue(exceptionClass);
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

	// credentials is of wrong format
	private static Matcher<DaDataClientException> matcherCredentialsBadFormat() {
		return allOf(hasProperty("httpStatusCode", is(401)),
		             hasProperty("httpStatusText", is(equalToIgnoringCase("UNAUTHORIZED"))),
		             hasProperty("apiErrorCode", is(CREDENTIALS_MISSING)),
		             hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		             hasProperty("apiErrorMessage", hasProperty("detail", is("Format of the given token seems invalid. Please, verify it in the profile page."))),
		             hasProperty("fatal", is(true)));
	}

	// credentials missing
	private static Matcher<DaDataClientException> matcherCredentialsMissing() {
		return allOf(hasProperty("httpStatusCode", is(401)),
		             hasProperty("httpStatusText", is(equalToIgnoringCase("UNAUTHORIZED"))),
		             hasProperty("apiErrorCode", is(CREDENTIALS_MISSING)),
		             hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		             hasProperty("apiErrorMessage", hasProperty("detail", anyOf(is("Учетные данные не были предоставлены."),
		                                                                        is("Недопустимый токен.")))),
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
		             hasProperty("apiErrorMessage", anyOf(hasProperty("detail", is("Too many items in request")),
		                                                  hasProperty("details", is(arrayContaining("Processing failed. Document should not contain more than 50 records."))))),
		             hasProperty("fatal", is(false)));
	}
/*
	private static Matcher<DaDataClientException> matcherTooManyItems1() {
		return allOf(hasProperty("httpStatusCode", is(400)),
		             hasProperty("httpStatusText", is(equalToIgnoringCase("BAD REQUEST"))),
		             hasProperty("apiErrorCode", is(BAD_REQUEST_FORMAT)),
		             hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		             hasProperty("apiErrorMessage", anyOf(hasProperty("detail", is("Too many items in request")),
		                                                  hasProperty("details", is(arrayContaining("Processing failed. Document should not contain more than 50 records."))))),
		             hasProperty("fatal", is(true)));
	}
*/

	// too many requests
	private static Matcher<DaDataClientException> matcherTooManyRequests() {
		return allOf(hasProperty("httpStatusCode", is(429)),
		             hasProperty("httpStatusText", is(equalToIgnoringCase("TOO MANY REQUESTS"))),
		             hasProperty("apiErrorCode", is(TOO_MANY_REQUESTS)),
		             hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		             hasProperty("apiErrorMessage", hasProperty("detail", is("Too many requests"))),
		             hasProperty("fatal", is(false)));
	}

	// empty response
	private static Matcher<DaDataException> matcherEmptyResponse1() {
		return hasProperty("fatal", is(false));
	}

	// message format error
	private static Matcher<DaDataException> matcherEmptyResponse4() {
		return allOf(hasProperty("cause", instanceOf(RestClientException.class)),
		             hasProperty("cause", hasProperty("message", is("Error while extracting response for type [class [Lru.redcom.software.util.integration.api.client.dadata.dto.Address;] and content type [application/json]; nested exception is org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Cannot deserialize instance of `ru.redcom.software.util.integration.api.client.dadata.dto.Address[]` out of START_OBJECT token; nested exception is com.fasterxml.jackson.databind.exc.MismatchedInputException: Cannot deserialize instance of `ru.redcom.software.util.integration.api.client.dadata.dto.Address[]` out of START_OBJECT token\n at [Source: (ByteArrayInputStream); line: 1, column: 1]"))),
		             hasProperty("fatal", is(false)));
	}

	private static Matcher<DaDataException> matcherEmptyResponse5() {
		return allOf(hasProperty("cause", instanceOf(RestClientException.class)),
		             hasProperty("cause", hasProperty("message", is("Error while extracting response for type [class [Lru.redcom.software.util.integration.api.client.dadata.dto.Address;] and content type [application/json]; nested exception is org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Cannot construct instance of `ru.redcom.software.util.integration.api.client.dadata.dto.Address` (although at least one Creator exists): no String-argument constructor/factory method to deserialize from String value (''); nested exception is com.fasterxml.jackson.databind.exc.MismatchedInputException: Cannot construct instance of `ru.redcom.software.util.integration.api.client.dadata.dto.Address` (although at least one Creator exists): no String-argument constructor/factory method to deserialize from String value ('')\n at [Source: (ByteArrayInputStream); line: 1, column: 2] (through reference chain: java.lang.Object[][0])"))),
		             hasProperty("fatal", is(false)));
	}

	private static Matcher<DaDataClientException> matcherInternalServerError() {
		return allOf(hasProperty("httpStatusCode", is(500)),
		             hasProperty("httpStatusText", is(equalToIgnoringCase("INTERNAL SERVER ERROR"))),
		             hasProperty("apiErrorCode", is(nullValue(APIErrorCode.class))),
		             hasProperty("apiErrorMessage", notNullValue(APIErrorMessage.class)),
		             hasProperty("apiErrorMessage", hasProperty("detail", is("Shit happens"))),
		             hasProperty("fatal", is(true)));
	}

	private static Matcher<DaDataClientException> matcherWrongErrorResponse() {
		//noinspection unchecked
		return allOf(hasProperty("httpStatusCode", is(400)),
		             hasProperty("httpStatusText", is(equalToIgnoringCase("BAD REQUEST"))),
		             hasProperty("apiErrorCode", is(BAD_REQUEST_FORMAT)),
		             hasProperty("apiErrorMessage", nullValue(APIErrorMessage.class)),
		             hasProperty("fatal", is(true)),
		             hasProperty("suppressed", allOf(arrayContaining(instanceOf(RestClientException.class)),
		                                             arrayContainingInAnyOrder(hasProperty("message", is("Error while extracting response for type [class ru.redcom.software.util.integration.api.client.dadata.dto.APIErrorMessage] and content type [application/json]; nested exception is org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Unexpected end-of-input: expected close marker for Object (start marker at [Source: (ByteArrayInputStream); line: 1, column: 1]); nested exception is com.fasterxml.jackson.core.io.JsonEOFException: Unexpected end-of-input: expected close marker for Object (start marker at [Source: (ByteArrayInputStream); line: 1, column: 1])\n" +
		                                                                                                 " at [Source: (ByteArrayInputStream); line: 1, column: 3]")))))
		            );
	}

	// =================================================================================================================
}
