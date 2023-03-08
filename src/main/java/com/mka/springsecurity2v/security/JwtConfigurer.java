package com.mka.springsecurity2v.security;

import lombok.AllArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

//для конфигурации токена
@Component
@AllArgsConstructor
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenFilter jwtTokenFilter;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        //настраиваем цепочку фильтров, здесь добавляем первым фильтром наш JwtTokenFilter
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
