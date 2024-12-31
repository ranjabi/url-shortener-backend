package com.ranjabi.urlshortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ranjabi.urlshortener.jwt.JwtAuthenticationFilter;
import com.ranjabi.urlshortener.user.UserDetailsServiceImpl;

@Configuration
public class SecurityConfig {
    private final UserDetailsServiceImpl userService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsServiceImpl userService, JwtAuthenticationFilter jwtAuthenticationFilter, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager manager) throws Exception {
        http
                .csrf(configurer -> configurer.disable()) // for POST requests via Postman
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/register", "/login").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    // bikin authenticationManager, yang punya DaoAuthenticationProvider (implement userDetailsService)
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userService);
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder);

        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider);

        return authenticationManagerBuilder.build();
    }

}
