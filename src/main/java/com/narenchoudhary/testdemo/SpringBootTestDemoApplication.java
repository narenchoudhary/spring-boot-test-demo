package com.narenchoudhary.testdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Bootstrap class
 * 
 * @author narenchoudhary
 *
 */
@SpringBootApplication
public class SpringBootTestDemoApplication {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootTestDemoApplication.class, args);
	}
}