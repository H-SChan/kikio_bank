package com.kakao.bank.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.*;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter implements Filter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}
