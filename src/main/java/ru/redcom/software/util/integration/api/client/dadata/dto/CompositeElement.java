/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.dto;

/**
 * Marker interface to specify that element can participate in composite request or response.
 */
//@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXTERNAL_PROPERTY)
//@JsonTypeIdResolver()
//@JsonTypeResolver()
// TODO replace with custom typeidresolver ?
//@JsonDeserialize(using = CompositeResponse.CompositeElementDeserializer.class)
//@JsonSubTypes({
//		@Type(value = AsIs.class, name = "AS_IS"),
//		@Type(value = Address.class, name = "ADDRESS"),
//		@Type(value = BirthDate.class, name = "BIRTHDATE"),
//		@Type(value = Email.class, name = "EMAIL"),
//		@Type(value = Name.class, name = "NAME"),
//		@Type(value = Passport.class, name = "PASSPORT"),
//		@Type(value = Phone.class, name = "PHONE"),
//		@Type(value = Vehicle.class, name = "VEHICLE")
//})
//@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "source", visible = true)
//@JsonTypeIdResolver(CompositeResponse.CompositeElementTypeIdResolver.class)
/*
@JsonDeserialize(using = CompositeResponse.CompositeElementDeserializer.class)
@JsonSubTypes({
		@Type(value = Address.class, name = "kladr_id"),
		@Type(value = BirthDate.class, name = "birthdate"),
		@Type(value = Email.class, name = "email"),
		@Type(value = Name.class, name = "surname"),
		@Type(value = Passport.class, name = "series"),
		@Type(value = Phone.class, name = "phone"),
		@Type(value = Vehicle.class, name = "brand"),
		@Type(value = AsIs.class, name = "source")
})
*/
//@JsonDeserialize(converter = CompositeResponse.CompositeElementConverter.class)
interface CompositeElement {

	// to be used in type id resolver
	CompositeElementType getCompositeElementType();
}
