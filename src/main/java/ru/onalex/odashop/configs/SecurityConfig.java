package ru.onalex.odashop.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import ru.onalex.odashop.services.CustomerService;

import static ru.onalex.odashop.controllers.GlobalControllerAdvice.MAIN_PAGE;

@Configuration
//@EnableWebSecurity(debug = true)
@EnableWebSecurity
public class SecurityConfig {


    private CustomerService customerService;

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/adminpanel/**").hasRole("ADMIN")
                        .requestMatchers("/customer/**").authenticated()
                        .requestMatchers("/customer/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/customer/login")
                        .loginProcessingUrl("/login")
                        .successHandler(savedRequestAwareAuthenticationSuccessHandler())
                        .permitAll()
                )
                .logout((logout) -> logout.logoutSuccessUrl(MAIN_PAGE))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(customerService); //привязываем процесс получения наших пользователей к провайдеру безопасности
        return authProvider;
    }

    private AuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl(MAIN_PAGE); // куда перенаправлять, если нет сохраненного запроса
        handler.setAlwaysUseDefaultTargetUrl(false); // использовать сохраненный URL, если он есть
        return handler;
    }
}