/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Factory class for DaData client implementations.
 */
public class DaDataClientFactory {
	// ------- Фабричные методы ----------------------------------------------------------------------------------------

	/**
	 * Конструктор с возможностью задания базового URI сервиса DaData.
	 * <p>Параметры <code>apiKey, secretKey</code> должны быть непустыми, иначе выдаётся исключение
	 * {@link IllegalArgumentException}.
	 * Если параметр <code>baseUri</code> null или пустой, будет использован адрес сервиса по умолчанию.
	 * </p>
	 * <p>
	 * Конструктор позволяет задать объект REST Template Builder, что может оказаться полезным в среде Spring Boot.
	 * </p>
	 *
	 * @param apiKey    Ключ API
	 * @param secretKey Пароль API
	 * @param baseUri   Строка базового URI
	 *
	 * @return Экземпляр клиента DaData API
	 */
	public static DaDataClient getInstance(@Nonnull final String apiKey, @Nonnull final String secretKey,
	                                       @Nullable String baseUri, @NonNull RestTemplateBuilder restTemplateBuilder) {
		Assert.notNull(restTemplateBuilder, "REST Template builder is null");
		Assert.isTrue(StringUtils.hasText(apiKey), "API Key is not set");
		Assert.isTrue(StringUtils.hasText(secretKey), "Secret Key is not set");
		return new DaDataClientImpl(apiKey, secretKey, baseUri, restTemplateBuilder);
	}

	/**
	 * Конструктор с возможностью задания базового URI сервиса DaData.
	 * <p>Параметры <code>apiKey, secretKey</code> должны быть непустыми, иначе выдаётся исключение
	 * {@link IllegalArgumentException}.
	 * Если параметр <code>baseUri</code> null или пустой, будет использован адрес сервиса по умолчанию.
	 * </p>
	 *
	 * @param apiKey    Ключ API
	 * @param secretKey Пароль API
	 * @param baseUri   Строка базового URI
	 *
	 * @return Экземпляр клиента DaData API
	 */
	@SuppressWarnings("WeakerAccess")
	public static DaDataClient getInstance(@Nonnull final String apiKey, @Nonnull final String secretKey, @Nullable String baseUri) {
		return getInstance(apiKey, secretKey, baseUri, new RestTemplateBuilder());
	}

	/**
	 * Конструктор с базовым URI сервиса DaData по умолчанию.
	 * <p>Параметры <code>apiKey, secretKey</code> должны быть непустыми, иначе выдаётся исключение
	 * {@link IllegalArgumentException}.</p>
	 *
	 * @param apiKey    Ключ API
	 * @param secretKey Пароль API
	 *
	 * @return Экземпляр клиента DaData API
	 */
	public static DaDataClient getInstance(@Nonnull final String apiKey, @Nonnull final String secretKey) {
		return getInstance(apiKey, secretKey, null);
	}
}
