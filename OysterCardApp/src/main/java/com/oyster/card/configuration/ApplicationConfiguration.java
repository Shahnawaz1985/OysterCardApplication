package com.oyster.card.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Shahnawaz
 */
@Configuration
@ComponentScan(basePackages = {"com.oyster.card.controller", 
		"com.oyster.card.services",
		"com.oyster.card.managers", 
		"com.oyster.card.utils"})
public class ApplicationConfiguration {
}
