package com.github.luismoramedina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@IntegrationComponentScan
public class RabbitIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitIntegrationApplication.class);
	}

}
