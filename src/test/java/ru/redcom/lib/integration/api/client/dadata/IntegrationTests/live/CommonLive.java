/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.lib.integration.api.client.dadata.IntegrationTests.live;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.DaDataClientFactory;

@Configuration
class CommonLive {
	static String API_KEY;
	static String SECRET_KEY;

	CommonLive(@Value("${dadata.api-key}") final String apiKey,
	           @Value("${dadata.secret-key}") final String secretKey) {
		API_KEY = apiKey;
		SECRET_KEY = secretKey;
	}

	@Bean
	DaDataClient dadataBean() {
		return DaDataClientFactory.getInstance(API_KEY, SECRET_KEY);
	}
}
