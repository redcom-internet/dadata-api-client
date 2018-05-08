/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata;

import ru.redcom.software.util.integration.api.client.dadata.dto.*;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

/**
 * DaData API Client interface.
 */
public interface DaDataClient {
	boolean checkAvailability(boolean silent) throws DaDataException;

	@Nonnull
	BigDecimal getProfileBalance() throws DaDataException;

	@Nonnull
	Address cleanAddress(@Nonnull String source) throws DaDataException;

	@Nonnull
	Address[] cleanAddresses(@Nonnull String... sources) throws DaDataException;

	@Nonnull
	Phone cleanPhone(@Nonnull String source) throws DaDataException;

	@Nonnull
	Phone[] cleanPhones(@Nonnull String... sources) throws DaDataException;

	@Nonnull
	Passport cleanPassport(@Nonnull String source) throws DaDataException;

	@Nonnull
	Passport[] cleanPassports(@Nonnull String... sources) throws DaDataException;

	@Nonnull
	Name cleanName(@Nonnull String source) throws DaDataException;

	@Nonnull
	Name[] cleanNames(@Nonnull String... sources) throws DaDataException;

	@Nonnull
	Email cleanEmail(@Nonnull String source) throws DaDataException;

	@Nonnull
	Email[] cleanEmails(@Nonnull String... sources) throws DaDataException;

	// TODO implement other API bindings
}
