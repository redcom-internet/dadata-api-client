/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata;

import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpMessageConverterExtractor;
import ru.redcom.lib.integration.api.client.dadata.dto.APIErrorMessage;

import java.io.IOException;
import java.util.Collections;

/**
 * Error handler to convert HTTP Client Request-related errors into DaDataClientException.
 *
 * @author boris
 */
class ClientErrorHandler extends DefaultResponseErrorHandler {
	@NonNull private final HttpMessageConverterExtractor<? extends APIErrorMessage> messageExtractor;


	ClientErrorHandler(@NonNull final HttpMessageConverter<?> messageConverter) {
		this.messageExtractor = new HttpMessageConverterExtractor<>(APIErrorMessage.class,
		                                                            Collections.singletonList(messageConverter));
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		val rawStatusCode = response.getRawStatusCode();
		val statusCode = HttpStatus.resolve(rawStatusCode);
		val statusText = response.getStatusText();

		// Extract error details from request body
		APIErrorMessage errorDetails = null;
		Exception resourceException = null;
		try {
			errorDetails = messageExtractor.extractData(response);
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

		val e = new DaDataClientException(message, rawStatusCode, statusText, errorCode, errorDetails, nestedException);
		if (resourceException != null)
			e.addSuppressed(resourceException);
		throw e;
	}
}
