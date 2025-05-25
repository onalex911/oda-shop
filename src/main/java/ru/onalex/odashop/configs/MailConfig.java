package ru.onalex.odashop.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;

@Configuration
public class MailConfig implements WebMvcConfigurer {
//    @Bean
//    public JavaMailSender javaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.yandex.ru");
//        mailSender.setPort(465);
//
//        mailSender.setUsername("onalex911@yandex.ru");
//        mailSender.setPassword("zyxhiolgrmqqkyht");
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtps");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.ssl.enable", "true");
//        props.put("mail.smtp.connectiontimeout", "5000");
//        props.put("mail.smtp.timeout", "5000");
//        props.put("mail.smtp.writetimeout", "5000");
//
//        return mailSender;
//    }
}