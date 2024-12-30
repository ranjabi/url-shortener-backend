package com.ranjabi.urlshortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ranjabi.urlshortener.security.AccessTokenAuthenticationProvider;
import com.ranjabi.urlshortener.security.AccessTokenFilter;
import com.ranjabi.urlshortener.user.UserService;

@Configuration
public class SecurityConfig {
    private final UserService userService;
    private final AccessTokenAuthenticationProvider accessTokenAuthenticationProvider;

    public SecurityConfig(UserService userService, AccessTokenAuthenticationProvider accessTokenAuthenticationProvider) {
        this.userService = userService;
        this.accessTokenAuthenticationProvider = accessTokenAuthenticationProvider;
    }
    

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager manager) throws Exception {
        http
                .csrf(configurer -> configurer.disable()) // for POST requests via Postman
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/register", "/login", "/token").permitAll()
                        .anyRequest().authenticated())
                .addFilterAfter(
                        new AccessTokenFilter(manager),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    // bikin authenticationManager, yang implement DaoAuthenticationProvider via
    // userDetailsService
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        // password encoder disetting di UserService

        authenticationManagerBuilder.authenticationProvider(this.accessTokenAuthenticationProvider);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider);

        return authenticationManagerBuilder.build();
    }

}
