package com.ranjabi.urlshortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UrlShortenerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerApplication.class, args);
	}

	@Bean
    public Customer customer(@Autowired @Qualifier("address2") String address) {
        return new Customer("Clara Foster", address);
    }

	@Bean
	// beans are initialized during the application startup
	public Customer temporary(@Autowired Customer customer) {
		System.out.println(customer);
		return customer;
	}
}
