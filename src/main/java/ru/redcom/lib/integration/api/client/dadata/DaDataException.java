/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata;

import lombok.Getter;
import org.springframework.lang.Nullable;


/**
 * DaData API exception base class.
 *
 * @author boris
 */
public class DaDataException extends RuntimeException {
	// True indicates that error is permanent
	@Getter
	private final boolean fatal;


	DaDataException(@Nullable final String message) {
		super(message);
		this.fatal = false;
	}

	DaDataException(@Nullable final String message, final boolean fatal) {
		super(message);
		this.fatal = fatal;
	}

	DaDataException(@Nullable final String message, @Nullable final Throwable cause, final boolean fatal) {
		super(message, cause);
		this.fatal = fatal;
	}
}
