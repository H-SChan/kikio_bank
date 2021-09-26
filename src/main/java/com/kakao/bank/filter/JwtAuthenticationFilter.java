package com.kakao.bank.filter;

import com.kakao.bank.domain.entity.User;
import com.kakao.bank.lib.AuthorizationExtractor;
import com.kakao.bank.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter implements Filter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    private JwtService jwtService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext());
        this.jwtService = ctx.getBean(JwtService.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            AuthorizationExtractor extractor = new AuthorizationExtractor();
            String token = extractor.extract((HttpServletRequest) request, "Bearer");

            if (!((HttpServletRequest) request).getMethod().equals("OPTIONS")) {
                if (StringUtils.isEmpty(token)) {
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "검증 오류");
                }
                String userId = jwtService.validToken(token);

                request.setAttribute("userId", userId);
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(
                    (HttpServletRequest) request,
                    (HttpServletResponse) response,
                    null,
                    e
            );
        }
    }

    @Override
    public void destroy() {

    }
}
