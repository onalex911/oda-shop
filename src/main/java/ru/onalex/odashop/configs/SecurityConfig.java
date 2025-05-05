package ru.onalex.odashop.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true) //возможность защиты отдельных методов
public class SecurityConfig {
//    private UserService userService;
//
//    @Autowired
//    public void setUserService(UserService userService) {
//        this.userService = userService;
//    }
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf().disable()
            .cors().disable()
            .authorizeRequests()
//            .requestMatchers("/api/secured/**").authenticated()
//            .requestMatchers("/api/info/**").authenticated()
//            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().permitAll();
//            .and()
//            .sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            .and()
//            .exceptionHandling()
//            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//            .and()
//            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
