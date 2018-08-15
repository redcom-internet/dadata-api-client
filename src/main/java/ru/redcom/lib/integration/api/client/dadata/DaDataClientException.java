/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata;

import lombok.Getter;
import org.springframework.lang.Nullable;
import ru.redcom.lib.integration.api.client.dadata.dto.APIErrorMessage;


/**
 * Client-related DaData API errors.
 *
 * @author boris
 */
@Getter
public class DaDataClientException extends DaDataException {
	private final int httpStatusCode;
	@Nullable private final String httpStatusText;
	@Nullable private final APIErrorCode apiErrorCode;
	@Nullable private final APIErrorMessage apiErrorMessage;


	DaDataClientException(@Nullable final String message, final int httpStatusCode, @Nullable String httpStatusText,
	                      @Nullable final APIErrorCode apiErrorCode, @Nullable final APIErrorMessage apiErrorMessage,
	                      @Nullable final Throwable cause) {
		super(message, cause, apiErrorCode == null || apiErrorCode.isFatal());
		this.httpStatusCode = httpStatusCode;
		this.httpStatusText = httpStatusText;
		this.apiErrorCode = apiErrorCode;
		this.apiErrorMessage = apiErrorMessage;
	}
}
