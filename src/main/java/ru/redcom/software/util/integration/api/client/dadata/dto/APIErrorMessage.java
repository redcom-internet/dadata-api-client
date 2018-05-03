/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Value;
import lombok.val;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/*
Error Response object examples:
- empty request body: ''
{
   "data" : [
      "Field is required"
   ]
}
- request body is an empty array: '[]'
{
   "detail" : "Bad request. Use non empty list."
}
- request body is an array with single empty string: '[""]'
{
   "details" : [
      "Request does not contain data for standartization"
   ]
}
- balance is exhaused:
{
   "detail" : "Zero balance"
}
 */
@Value
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE)
public class APIErrorMessage implements Serializable {
	@Nonnull private final Map<String, Object> contents = new HashMap<>();

	@SuppressWarnings("unused")
	@Nonnull
	@JsonAnyGetter
	public Map<String, Object> getContents() {
		return contents;
	}

	@SuppressWarnings("unused")
	@JsonAnySetter
	private void setContents(@Nonnull final String name, @Nullable final Object value) {
		contents.put(name, value);
	}

	@Nullable
	private String[] getContentsKeyForArray(@Nonnull final String key) {
		val o = contents.get(key);
		return o instanceof String[] ? (String[]) o : o instanceof Collection ? ((Collection<?>) o).stream().map(Object::toString).toArray(String[]::new) : null;
	}

	// Convenience methods
	@SuppressWarnings("unused")
	@Nullable
	public String getDetail() {
		val o = contents.get("detail");
		return o instanceof String ? (String) o : null;
	}

	@SuppressWarnings("unused")
	@Nullable
	public String[] getDetails() {
		return getContentsKeyForArray("details");
	}

	@SuppressWarnings("unused")
	@Nullable
	public String[] getData() {
		return getContentsKeyForArray("data");
	}
}
