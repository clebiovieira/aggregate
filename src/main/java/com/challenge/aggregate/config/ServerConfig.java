package com.challenge.aggregate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "server")
@Data
public class ServerConfig {
	String url;
	String textDatabase;
}
