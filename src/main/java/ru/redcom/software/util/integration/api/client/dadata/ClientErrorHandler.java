/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpMessageConverterExtractor;
import ru.redcom.software.util.integration.api.client.dadata.dto.APIErrorMessage;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// Error handler to convert HTTP Client Request-related errors into DaDataClientException
class ClientErrorHandler extends DefaultResponseErrorHandler {
	private final List<HttpMessageConverter<?>> messageConverters;

	ClientErrorHandler(@Nonnull final HttpMessageConverter<?>... messageConverters) {
		this.messageConverters = Arrays.asList(messageConverters);
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		final int rawStatusCode = response.getRawStatusCode();
		final HttpStatus statusCode = HttpStatus.resolve(rawStatusCode);
		final String statusText = response.getStatusText();

		// Extract error details from request body
		APIErrorMessage errorDetails = null;
		Exception resourceException = null;
		try {
			// System.out.println("response body: " + new BufferedReader(new InputStreamReader(response.getBody())).lines().collect(Collectors.joining("\n")));
			final HttpMessageConverterExtractor<? extends APIErrorMessage> extractor =
					new HttpMessageConverterExtractor<>(APIErrorMessage.class, this.messageConverters);
			errorDetails = extractor.extractData(response);
		} catch (Exception e) {
			resourceException = e;
		}

		// Decode HTTP error
		APIErrorCode errorCode = null;
		String message = "Unknown HTTP error";
		if (statusCode != null) {
			switch (statusCode.series()) {
				case CLIENT_ERROR:
					errorCode = APIErrorCode.fromHttpStatus(statusCode);
					message = errorCode != null ? errorCode.getMessage() : "HTTP Client error";
					break;
				case SERVER_ERROR:
					message = "HTTP Server error";
					break;
			}
		}

		// Get framework exception, just for someone might need it
		Exception nestedException = null;
		try {
			super.handleError(response);
		} catch (Exception e) {
			nestedException = e;
		}

		final DaDataClientException e = new DaDataClientException(message, rawStatusCode, statusText, errorCode, errorDetails, nestedException);
		if (resourceException != null)
			e.addSuppressed(resourceException);
		throw e;
	}
}
