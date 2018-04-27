/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata;

import lombok.Getter;

import javax.annotation.Nullable;

public class DaDataException extends RuntimeException {
	@Getter
	protected final boolean fatal;

	DaDataException(@Nullable final String message) {
		super(message);
		this.fatal = false;
	}

	DaDataException(@Nullable final String message, @Nullable final Throwable cause) {
		super(message, cause);
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
