/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

@SuppressWarnings("unused")
public enum APIErrorCode {
	// 400	Некорректный запрос:
	//          Невалидный JSON.
	//          Отсутствуют обязательные параметры structure или data.
	//          В structure указан неподдерживаемый тип.
	//          Количество полей в записях более указанного в structure.
	BAD_REQUEST_FORMAT(HttpStatus.BAD_REQUEST, "Incorrect request format or data type", true),
	// 401	В запросе отсутствует API-ключ или секретный ключ
	CREDENTIALS_MISSING(HttpStatus.UNAUTHORIZED, "API key or Secret key are missing", true),
	// 402	Недостаточно средств для обработки запроса, пополните баланс
	INSUFFICIENT_BALANCE(HttpStatus.PAYMENT_REQUIRED, "Insufficient balance", false),
	// 403	В запросе указан несуществующий ключ
	CREDENTIALS_INVALID(HttpStatus.FORBIDDEN, "Access denied or API/Secret key is invalid", true),
	// 405	Запрос сделан с методом, отличным от POST
	UNSUPPORTED_METHOD(HttpStatus.NOT_ACCEPTABLE, "Unsupported request method", true),
	// 413	Запрос содержит более 10 записей
	TOO_MANY_ITEMS(HttpStatus.PAYLOAD_TOO_LARGE, "Too many items requested", false),
	// 429	Слишком много запросов в секунду
	TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "Too many requests per second", false);

	@Nonnull private final HttpStatus statusCode;
	@Getter
	@Nonnull
	private final String message;
	@Getter
	private final boolean fatal;

	APIErrorCode(@Nonnull final HttpStatus statusCode, @NonNull final String message, final boolean fatal) {
		this.statusCode = statusCode;
		this.message = message;
		this.fatal = fatal;
	}

	@Nullable
	static APIErrorCode fromHttpStatus(@Nonnull final HttpStatus statusCode) {
		return Arrays.stream(APIErrorCode.values()).filter(v -> v.statusCode == statusCode).findAny().orElse(null);
	}
}
