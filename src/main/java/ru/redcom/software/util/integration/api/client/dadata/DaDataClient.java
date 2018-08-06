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
	/**
	 * Contact the API status endpoint to check availability.
	 * Uses only API key to identify the account.
	 * Does not validate Secret key.
	 *
	 * @param silent Silent mode. Do not throws exceptions if set to true.
	 *
	 * @return <code>true</code> if API is available, <code>false</code> otherwise.
	 *
	 * @throws DaDataException On errors, if not suppressed by the silent mode
	 */
	boolean checkAvailability(boolean silent) throws DaDataException;

	/**
	 * Retrieve current account profile balance.
	 *
	 * @return Balance amount (currently in RUR only)
	 * @throws DaDataException  On API access errors
	 */
	@Nonnull
	BigDecimal getProfileBalance() throws DaDataException;

	// ------- Standardization API access methods ----------------------------------------------------------------------

	/**
	 * Clean a single address.
	 *
	 * @param source    Source address
	 * @return Cleaned address object
	 * @throws DaDataException  On various API errors
	 */
	@Nonnull
	Address cleanAddress(@Nonnull String source) throws DaDataException;

	/**
	 * Clean multiple addresses at once.
	 *
	 * @param sources    Source address strings array
	 * @return Cleaned address objects
	 * @throws DaDataException  On various API errors
	 */
	@Nonnull
	Address[] cleanAddresses(@Nonnull String... sources) throws DaDataException;

	/**
	 * Clean a single phone number.
	 *
	 * @param source    Source phone number
	 * @return Cleaned phone object
	 * @throws DaDataException  On various API errors
	 */
	@Nonnull
	Phone cleanPhone(@Nonnull String source) throws DaDataException;

	/**
	 * Clean multiple phone numbers at once.
	 *
	 * @param sources    Source phone number strings array
	 * @return Cleaned phone objects
	 * @throws DaDataException  On various API errors
	 */
	@Nonnull
	Phone[] cleanPhones(@Nonnull String... sources) throws DaDataException;

	/**
	 * Clean a single passport number.
	 *
	 * @param source    Source passport number
	 * @return Cleaned passport object
	 * @throws DaDataException  On various API errors
	 */
	@Nonnull
	Passport cleanPassport(@Nonnull String source) throws DaDataException;

	/**
	 * Clean multiple passport numbers at once.
	 *
	 * @param sources    Source passport number strings array
	 * @return Cleaned passport objects
	 * @throws DaDataException  On various API errors
	 */
	@Nonnull
	Passport[] cleanPassports(@Nonnull String... sources) throws DaDataException;

	/**
	 * Clean a single name.
	 *
	 * @param source    Source name
	 * @return Cleaned name object
	 * @throws DaDataException  On various API errors
	 */
	@Nonnull
	Name cleanName(@Nonnull String source) throws DaDataException;

	/**
	 * Clean multiple names at once.
	 *
	 * @param sources    Source name strings array
	 * @return Cleaned name objects
	 * @throws DaDataException  On various API errors
	 */
	@Nonnull
	Name[] cleanNames(@Nonnull String... sources) throws DaDataException;

	/**
	 * Clean a single E-mail address.
	 *
	 * @param source    Source E-mail address
	 * @return Cleaned E-mail address object
	 * @throws DaDataException  On various API errors
	 */
	@Nonnull
	Email cleanEmail(@Nonnull String source) throws DaDataException;

	/**
	 * Clean multiple E-mail addresses at once.
	 *
	 * @param sources    Source E-mail address strings array
	 * @return Cleaned E-mail address objects
	 * @throws DaDataException  On various API errors
	 */
	@Nonnull
	Email[] cleanEmails(@Nonnull String... sources) throws DaDataException;

	/**
	 * Clean a single birth date.
	 *
	 * @param source    Source birth date string
	 * @return Cleaned birth date object
	 * @throws DaDataException  On various API errors
	 */
	@Nonnull
	BirthDate cleanBirthDate(@Nonnull String source) throws DaDataException;

	/**
	 * Clean multiple birth dates at once.
	 *
	 * @param sources    Source birth date strings array
	 * @return Cleaned birth date objects
	 * @throws DaDataException  On various API errors
	 */
	@Nonnull
	BirthDate[] cleanBirthDates(@Nonnull String... sources) throws DaDataException;

	/**
	 * Clean a single vehicle model.
	 *
	 * @param source    Source vehicle model name string
	 * @return Cleaned vehicle object
	 * @throws DaDataException  On various API errors
	 */
	@Nonnull
	Vehicle cleanVehicle(@Nonnull String source) throws DaDataException;

	/**
	 * Clean multiple vehicle models at once.
	 *
	 * @param sources    Source vehicle model strings array
	 * @return Cleaned vehicle objects
	 * @throws DaDataException  On various API errors
	 */
	@Nonnull
	Vehicle[] cleanVehicles(@Nonnull String... sources) throws DaDataException;

	/**
	 * Clean a collection of records consisting by various information types.
	 *
	 * @param source    Source composite request object
	 * @return Composite response object with cleaned records
	 * @throws DaDataException  On various API errors
	 */
	@Nonnull
	CompositeResponse cleanComposite(@Nonnull CompositeRequest source) throws DaDataException;
}
