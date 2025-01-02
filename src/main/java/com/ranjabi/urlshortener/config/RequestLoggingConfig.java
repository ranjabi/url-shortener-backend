package com.ranjabi.urlshortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class RequestLoggingConfig {

    public class CustomRequestLoggingFilter extends AbstractRequestLoggingFilter {

        @Override
        protected void beforeRequest(HttpServletRequest httpServletRequest, String message) {
            this.logger.debug(message);
        }

        @Override
        protected void afterRequest(HttpServletRequest httpServletRequest, String message) {

        }
    }

    @Bean
    public CustomRequestLoggingFilter requestLoggingFilter() {
        CustomRequestLoggingFilter loggingFilter = new CustomRequestLoggingFilter();

        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setIncludeHeaders(true);
        loggingFilter.setMaxPayloadLength(10000);

        return loggingFilter;
    }
}
