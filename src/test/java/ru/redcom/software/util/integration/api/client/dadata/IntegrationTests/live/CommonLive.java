/*
 * Copyright (c) 2018 Boris Fox, REDCOM-Internet CJSC
 * All rights reserved.
 */

package ru.redcom.software.util.integration.api.client.dadata.IntegrationTests.live;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClient;
import ru.redcom.software.util.integration.api.client.dadata.DaDataClientFactory;

@Configuration
class CommonLive {
	@Value("${dadata.api-key}")
	private String apiKey;
	@Value("${dadata.secret-key}")
	private String secretKey;


	@Bean
	DaDataClient dadataBean() {
		return DaDataClientFactory.getInstance(apiKey, secretKey);
	}
}
