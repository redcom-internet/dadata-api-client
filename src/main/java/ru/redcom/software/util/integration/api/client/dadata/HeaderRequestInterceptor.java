/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import javax.annotation.Nonnull;
import java.io.IOException;

// Request Interceptor to add credentials headers
class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {
	@Nonnull private final String headerName;
	@Nonnull private final String headerValue;

	HeaderRequestInterceptor(@Nonnull String headerName, @Nonnull String headerValue) {
		this.headerName = headerName;
		this.headerValue = headerValue;
	}

	@Override
	@Nonnull
	public ClientHttpResponse intercept(@Nonnull HttpRequest request, @Nonnull byte[] body,
	                                    @Nonnull ClientHttpRequestExecution execution) throws IOException {
		request.getHeaders().set(headerName, headerValue);
		return execution.execute(request, body);
	}
}
