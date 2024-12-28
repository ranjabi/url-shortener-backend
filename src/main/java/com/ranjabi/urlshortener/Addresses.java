package com.ranjabi.urlshortener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Addresses {

    @Bean
    public String address1() {
        return "Green Street, 102";
    }

    @Bean
    public String address2() {
        return "Apple Street, 15";
    }

}
