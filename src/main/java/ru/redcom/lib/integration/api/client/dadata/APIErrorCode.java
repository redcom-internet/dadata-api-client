/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Arrays;

/**
 * Several documented errors from DaData API specification.
 *
 * @author boris
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum APIErrorCode {
	/**
	 * <p>Status Code 400.<br>
	 * Generic status for various client request errors.</p>
	 * Example reasons:
	 * <ul>
	 * <li>
	 * Invalid JSON format
	 * </li>
	 * <li>
	 * Missing required fields
	 * </li>
	 * <li>
	 * Unsupported request item type
	 * </li>
	 * <li>
	 * Misaligned record structure
	 * </li>
	 * </ul>
	 */
	// 400	Некорректный запрос:
	//          Невалидный JSON.
	//          Отсутствуют обязательные параметры structure или data.
	//          В structure указан неподдерживаемый тип.
	//          Количество полей в записях более указанного в structure.
	BAD_REQUEST_FORMAT(HttpStatus.BAD_REQUEST, "Incorrect request format or data type", true),
	/**
	 * <p>Status Code 401.<br>
	 * Credentials (API key or Secret key) is missing from request headers.</p>
	 */
	// 401	В запросе отсутствует API-ключ или секретный ключ
	CREDENTIALS_MISSING(HttpStatus.UNAUTHORIZED, "API key or Secret key are missing", true),
	/**
	 * <p>Status Code 402.<br>
	 * Account profile balance is insufficient to process the request.</p>
	 */
	// 402	Недостаточно средств для обработки запроса, пополните баланс
	INSUFFICIENT_BALANCE(HttpStatus.PAYMENT_REQUIRED, "Insufficient balance", false),
	/**
	 * <p>Status Code 403.<br>
	 * Returned on invalid credentials (API key or Secret key is of wrong format or unknown to service).</p>
	 */
	// 403	В запросе указан несуществующий ключ
	CREDENTIALS_INVALID(HttpStatus.FORBIDDEN, "Access denied or API/Secret key is invalid", true),
	/**
	 * <p>Status Code 405.<br>
	 * Returned on requests with method other than POST.</p>
	 */
	// 405	Запрос сделан с методом, отличным от POST
	UNSUPPORTED_METHOD(HttpStatus.NOT_ACCEPTABLE, "Unsupported request method", true),
	/**
	 * <p>Status Code 413.<br>
	 * Request contains too many items.</p>
	 * May be returned when request contains more than 10 items or records to process.
	 */
	// 413	Запрос содержит более 10 записей
	TOO_MANY_ITEMS(HttpStatus.PAYLOAD_TOO_LARGE, "Too many items requested", false),
	/**
	 * <p>Status Code 429.<br>
	 * Too many requests per second.</p>
	 * May be returned during periods of service congestion.
	 */
	// 429	Слишком много запросов в секунду
	TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "Too many requests per second", false);

	@NonNull private final HttpStatus statusCode;
	@NonNull private final String message;
	private final boolean fatal;


	@Nullable
	static APIErrorCode fromHttpStatus(@NonNull final HttpStatus statusCode) {
		return Arrays.stream(APIErrorCode.values()).filter(v -> v.statusCode == statusCode).findAny().orElse(null);
	}
}
