package ru.onalex.odashop.controllers;

import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailTestController {

    private final JavaMailSender mailSender;
//    @Autowired
//    public MailTestController(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }

    @GetMapping("/test-mail")
    public String testMail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("onalex911@yandex.ru");  // Явно указываем отправителя
            message.setTo("onalex911@gmail.com");     // Проверенный email получателя
            message.setSubject("Test SMTP");
            message.setText("This is a test email.");

            mailSender.send(message);
            return "Email sent successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}