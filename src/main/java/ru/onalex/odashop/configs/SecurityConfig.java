package ru.onalex.odashop.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.onalex.odashop.entities.Customer;
import ru.onalex.odashop.repositories.CustomerRepository;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers("/assets/**", "/static/**", "/public/**", "/resources/**").permitAll()
//                        .requestMatchers("/assets/**").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/customer/**").authenticated()
//                        .requestMatchers("/adminlte/**").hasRole("ADMIN")
//                        .requestMatchers("/api/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
//                        .loginPage("/login")
                        .loginPage("/account")
                        .permitAll()
                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(CustomerRepository customerRepository) {
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("securepassword"))
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(admin);
        return username -> {
            Customer user = customerRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.emptyList()
            );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}