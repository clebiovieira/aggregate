package com.challenge.aggregate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.challenge.aggregate.config.ServerConfig;

@SpringBootApplication
@EnableConfigurationProperties(ServerConfig.class)
public class AggegateApplication {
	public static void main(String[] args) {
		SpringApplication.run(AggegateApplication.class, args);
	}
}
