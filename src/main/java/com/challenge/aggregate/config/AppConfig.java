package com.challenge.aggregate.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	@Bean(name = "restTemplate")
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
