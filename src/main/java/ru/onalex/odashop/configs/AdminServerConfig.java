package ru.onalex.odashop.configs;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@EnableAdminServer
public class AdminServerConfig {
//    @Bean
//    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .securityMatcher("/admin/**") // Админ-панель работает только по /admin/**
//                .authorizeHttpRequests(auth -> auth.anyRequest().hasRole("ADMIN"))
//                .formLogin(Customizer.withDefaults())
//                .build();
//    }
}

