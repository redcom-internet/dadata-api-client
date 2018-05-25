/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

/**
 * Element types of composite request
 */
@Getter
@RequiredArgsConstructor
public enum CompositeElementType {
	AS_IS(AsIs.class),
	NAME(Name.class),
	ADDRESS(Address.class),
	BIRTHDATE(BirthDate.class),
	PASSPORT(Passport.class),
	PHONE(Phone.class),
	EMAIL(Email.class),
	VEHICLE(Vehicle.class);

	@Nonnull private final Class<? extends CompositeElement> type;
}
