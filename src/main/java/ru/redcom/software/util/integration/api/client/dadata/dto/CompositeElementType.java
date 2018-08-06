/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

/**
 * Element types of composite request
 */
@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CompositeElementType {
	/** String payload copied from request to response without modification */
	AS_IS(AsIs.class),
	/** Person Name */
	NAME(Name.class),
	/** Geographical Address */
	ADDRESS(Address.class),
	/** Person Birth Date */
	BIRTHDATE(BirthDate.class),
	/** Passport Number */
	PASSPORT(Passport.class),
	/** Phone Number */
	PHONE(Phone.class),
	/** E-mail address */
	EMAIL(Email.class),
	/** Vehicle type */
	VEHICLE(Vehicle.class);

	@Nonnull private final Class<? extends ResponseItem> type;
}
