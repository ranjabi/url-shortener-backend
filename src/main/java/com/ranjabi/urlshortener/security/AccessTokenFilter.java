package com.ranjabi.urlshortener.security;

import java.io.IOException;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AccessTokenFilter extends OncePerRequestFilter {
    private final RequestMatcher matcher = new AntPathRequestMatcher("/action", HttpMethod.POST.name());
    private final AuthenticationEntryPoint authenticationEntryPoint = (request, response, ex) -> {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(ex.getMessage());
    };
    private final AuthenticationManager manager;

    public AccessTokenFilter(AuthenticationManager manager) {
        this.manager = manager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (matcher.matches(request)) {
            try {
                var token = request.getHeader("Access-Token");
                if (token != null) {
                    Authentication authentication = new AccessTokenAuthentication(token);
                    authentication = manager.authenticate(authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                    return;
                }
                throw new BadCredentialsException("Access token is required");
            } catch (AuthenticationException e) {
                authenticationEntryPoint.commence(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
