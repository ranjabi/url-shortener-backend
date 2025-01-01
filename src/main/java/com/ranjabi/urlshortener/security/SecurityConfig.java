package com.ranjabi.urlshortener.security;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ranjabi.urlshortener.dto.response.ErrorResponse;
import com.ranjabi.urlshortener.user.UserDetailsServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {
    private final UserDetailsServiceImpl userService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    public SecurityConfig(UserDetailsServiceImpl userService, JwtAuthenticationFilter jwtAuthenticationFilter,
            HandlerExceptionResolver handlerExceptionResolver,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager manager) throws Exception {

        http
                .exceptionHandling((exception) -> exception.authenticationEntryPoint(authenticationEntryPoint()))
                .csrf(configurer -> configurer.disable()) // for POST requests via Postman
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/register", "/login").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

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

    public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                AuthenticationException authException) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ErrorResponse<Void> errorResponse = new ErrorResponse<Void>("Authentication Failed: Missing or Invalid Credentials");

            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}
