package com.mka.springsecurity2v.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

//фильтр для jwt токенов
@Component
@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);  //получаем токен из заголовка запроса
        try {
            //проверяем токен на валидность и то, что он не пустой
            if (token != null && jwtTokenProvider.validateToken(token)) {
                //если токен валидный, создаём Authentication для контекста security
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    //добавляем Authentication в контекст security
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            ((HttpServletResponse) servletResponse).sendError(e.getHttpStatus().value());
            throw new JwtAuthenticationException("JWT is expired or invalid");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
