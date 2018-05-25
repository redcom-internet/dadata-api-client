/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

// see https://github.com/FasterXML/jackson-databind/issues/1627
// Until better way of type resolution will be found.
public class PresentPropertyPolymorphicDeserializer<T> extends StdDeserializer<T> {

	private final Map<String, Class<?>> propertyNameToType;

	public PresentPropertyPolymorphicDeserializer(Class<T> vc) {
		super(vc);
		final JsonSubTypes jsonSubTypes = vc.getAnnotation(JsonSubTypes.class);
		this.propertyNameToType = jsonSubTypes == null ? Collections.emptyMap() :
		                          Arrays.stream(jsonSubTypes.value())
		                                .collect(Collectors.toMap(JsonSubTypes.Type::name, JsonSubTypes.Type::value,
		                                                          (u, v) -> {
			                                                          throw new IllegalStateException(String.format("Duplicate key %s", u));
		                                                          },
		                                                          LinkedHashMap::new));
	}

	@Override
	public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		ObjectMapper objectMapper = (ObjectMapper) p.getCodec();
		ObjectNode object = objectMapper.readTree(p);
		for (String propertyName : propertyNameToType.keySet()) {
			if (object.has(propertyName))
				return deserialize(objectMapper, propertyName, object);
		}

		throw new IllegalArgumentException("could not infer to which class to deserialize " + object);
	}

	@SuppressWarnings("unchecked")
	private T deserialize(ObjectMapper objectMapper,
	                      String propertyName,
	                      ObjectNode object) throws IOException {
		return (T) objectMapper.treeToValue(object, propertyNameToType.get(propertyName));
	}
}
