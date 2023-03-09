package com.mka.springsecurity2v.config;

import com.mka.springsecurity2v.security.JwtConfigurer;
import com.mka.springsecurity2v.security.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//для указания, что ограничения у меня прописаны на методах, тогда antMatchers`ы нам не нужны
@EnableMethodSecurity
@AllArgsConstructor
public class SpringSecurityConfig {
    private final JwtConfigurer jwtConfigurer;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  //защита от csrf угроз (отключена)
                //не используем сессии
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()  //говорим - авторизовать следующие запросы
                .requestMatchers("/").permitAll()  //разрешить всем
                .requestMatchers("/api/v1/auth/login").permitAll()
                .requestMatchers("/api/v1/registration").permitAll()
                .anyRequest().authenticated()  //говорим - эти запросы должны быть аутентифицированы
                .and()
                .apply(jwtConfigurer);  //используем настроенную фильтрацию

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationProvider... authenticationProviders) {
        return new ProviderManager(authenticationProviders);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);  //12 - сила кодирования
    }
}
