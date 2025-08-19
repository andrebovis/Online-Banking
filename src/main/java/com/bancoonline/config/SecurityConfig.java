package com.bancoonline.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // desativa CSRF para testes com curl
            .authorizeHttpRequests()
            .anyRequest().permitAll(); // libera todas as requisições
        return http.build();
    }
}
