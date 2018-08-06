/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Value;
import lombok.val;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/*
Several samples ever noticed:

- on empty request body: ''
{
   "data" : [
      "Field is required"
   ]
}
- on request body with empty array: '[]'
{
   "detail" : "Bad request. Use non empty list."
}
- on request body with array containing single empty string: '[""]'
{
   "details" : [
      "Request does not contain data for standartization"
   ]
}
- on balance exhaused condition:
{
   "detail" : "Zero balance"
}
*/

/**
 * Data Transfer Object for API error response body.
 */
@Value
public class APIErrorMessage implements Serializable {
	@Nonnull private final Map<String, Object> contents = new HashMap<>();

	/**
	 * Get response contents as the map.
	 *
	 * @return Map with response contents tree
	 */
	@SuppressWarnings("unused")
	@Nonnull
	@JsonAnyGetter
	public Map<String, Object> getContents() {
		return contents;
	}

	// Setter method for deserialization
	@SuppressWarnings("unused")
	@JsonAnySetter
	private void setContents(@Nonnull final String name, @Nullable final Object value) {
		contents.put(name, value);
	}

	// Helper for flatting contents to string array
	@Nullable
	private String[] getContentsKeyForArray(@Nonnull final String key) {
		val o = contents.get(key);
		return o instanceof String[] ? (String[]) o : o instanceof Collection ? ((Collection<?>) o).stream().map(Object::toString).toArray(String[]::new) : null;
	}

	// Convenience getters

	/**
	 * Get detail element contents.
	 *
	 * @return Detail value if it's a string, null otherwise
	 */
	@SuppressWarnings("unused")
	@Nullable
	public String getDetail() {
		val o = contents.get("detail");
		return o instanceof String ? (String) o : null;
	}

	/**
	 * Get details element contents.
	 *
	 * @return Details contents as a string array, nested collections flatten to string representations
	 */
	@SuppressWarnings("unused")
	@Nullable
	public String[] getDetails() {
		return getContentsKeyForArray("details");
	}

	/**
	 * Get data element contents.
	 *
	 * @return Data contents as a string array, nested collections flatten to string representations
	 */
	@Nullable
	public String[] getData() {
		return getContentsKeyForArray("data");
	}
}
