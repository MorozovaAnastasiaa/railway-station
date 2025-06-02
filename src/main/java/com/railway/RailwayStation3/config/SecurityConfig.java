package com.railway.RailwayStation3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация безопасности Spring Security.
 * Настраивает доступ к страницам и API по-разному:
 * - /api/** — без авторизации (для REST)
 * - Остальные пути — через форму входа
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Безопасность для REST API (/api/**).
     * Полностью отключает защиту, сессии и CSRF.
     *
     * @param http HTTP security builder
     * @return настроенная цепочка фильтров
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.disable());

        return http.build();
    }

    /**
     * Безопасность для веб-интерфейса.
     * Требует аутентификации для всех страниц, кроме /login и /register.
     *
     * @param http HTTP security builder
     */
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/style.css").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler((request, response, auth) -> {
                            response.sendRedirect("/");
                        })
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                );

        return http.build();
    }

    /**
     * Бин для шифрования паролей.
     * Используется при регистрации и сохранении пользователей.
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}