package com.kakao.bank.config;

import com.kakao.bank.filter.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class JwtFilterConfig {

    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtFilterConfig(HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> authFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new JwtAuthenticationFilter(handlerExceptionResolver));
        registrationBean.addUrlPatterns("/user/*");
        registrationBean.addUrlPatterns("/account/*");
        registrationBean.setOrder(2);

        return registrationBean;
    }
}
