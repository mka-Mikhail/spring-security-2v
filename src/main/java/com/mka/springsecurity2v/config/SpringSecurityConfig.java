package com.mka.springsecurity2v.config;

import com.mka.springsecurity2v.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
//для указания, что ограничения у меня прописаны на методах, тогда antMatchers`ы нам не нужны
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  //защита от csrf угроз (отключена)
                .authorizeRequests()  //говорим - авторизовать следующие запросы
                .antMatchers("/").permitAll()  //разрешить всем
                .anyRequest().authenticated()  //говорим - эти запросы должны быть аутентифицированы
                .and()
                .formLogin()
                .loginPage("/auth/login").permitAll()  //страница для входа доступная для всех
                .defaultSuccessUrl("/auth/success")  //страница по умолчанию после входа
                .and()
                .logout()  //настроить выход
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))  //адрес и метод для выхода
                .invalidateHttpSession(true)  //инвалидируем сессию
                .clearAuthentication(true)  //очищаем аутентификацию
                .deleteCookies("JSESSIONID")  //удаляем JSESSIONID
                .logoutSuccessUrl("/auth/login");  //куда переходить при выходе
    }

    //создаём сервис для хранения пользователей (который здесь и создаём) в оперативной памяти без БД
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("admin"))  //кодируем пароль для хранения его в оперативке
                        .authorities(Role.ADMIN.getAuthorities())  //выставляем ограничения для юзера согласно его роли
                        .build(),
                User.builder()
                        .username("user")
                        .password(passwordEncoder().encode("user"))
                        .authorities(Role.USER.getAuthorities())
                        .build()
        );
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);  //12 - сила кодирования
    }
}
