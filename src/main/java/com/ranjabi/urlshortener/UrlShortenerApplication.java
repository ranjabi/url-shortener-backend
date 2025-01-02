package com.ranjabi.urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UrlShortenerApplication {

	public static void main(String[] args) {
		System.out.println("spring.datasource.url:" + System.getenv("spring.datasource.url"));
		System.out.println("POSTGRES_HOST:" + System.getenv("POSTGRES_HOST"));
		SpringApplication.run(UrlShortenerApplication.class, args);
	}

}
