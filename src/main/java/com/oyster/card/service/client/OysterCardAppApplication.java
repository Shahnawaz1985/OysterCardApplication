package com.oyster.card.service.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.oyster.card.configuration"})
public class OysterCardAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(OysterCardAppApplication.class, args);
	}

}
