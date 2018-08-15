/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata;

import lombok.experimental.UtilityClass;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


/**
 * Factory class for obtaining DaData client instances.
 * Only one implementation available currently, so no any configuration options for now.
 *
 * @author boris
 */
@UtilityClass
public class DaDataClientFactory {

	/**
	 * <p>Terse factory method with only credentials required.</p>
	 * Uses well-known default base service URI and new REST Template Builder instance.<br>
	 * Null or empty credentials results in {@link IllegalArgumentException}.
	 *
	 * @param apiKey    API key
	 * @param secretKey Secret key
	 *
	 * @return DaData API client instance
	 */
	public DaDataClient getInstance(@NonNull final String apiKey, @NonNull final String secretKey) {
		return getInstance(apiKey, secretKey, null);
	}

	/**
	 * <p>More verbose method allowing to customize base service URI.</p>
	 * Null <code>baseUri</code> value is treated as default.<br>
	 * Null or empty credentials results in {@link IllegalArgumentException}.
	 *
	 * @param apiKey    API key
	 * @param secretKey Secret key
	 * @param baseUri   Base URI string
	 *
	 * @return DaData API client instance
	 */
	public DaDataClient getInstance(@NonNull final String apiKey, @NonNull final String secretKey, @Nullable String baseUri) {
		return getInstance(apiKey, secretKey, baseUri, new RestTemplateBuilder());
	}

	/**
	 * <p>Most verbose method allowing to set both base URI and existing REST Template Builder instance.
	 * May be useful in Spring Boot environment to reuse a preconfigured REST Template Builder bean.</p>
	 * Null <code>baseUri</code> value is treated as default.<br>
	 * Null or empty credentials, as well as null <code>restTemplateBuilder</code> results
	 * in {@link IllegalArgumentException}.
	 *
	 * @param apiKey    API key
	 * @param secretKey Secret key
	 * @param baseUri   Base URI string
	 * @param restTemplateBuilder REST Template Builder instance to use
	 *
	 * @return DaData API client instance
	 */
	public DaDataClient getInstance(@NonNull final String apiKey, @NonNull final String secretKey,
	                                @Nullable String baseUri, @NonNull RestTemplateBuilder restTemplateBuilder) {
		Assert.notNull(restTemplateBuilder, "REST Template builder is null");
		Assert.isTrue(StringUtils.hasText(apiKey), "API Key is not set");
		Assert.isTrue(StringUtils.hasText(secretKey), "Secret Key is not set");
		return new DaDataClientImpl(apiKey, secretKey, baseUri, restTemplateBuilder);
	}
}
