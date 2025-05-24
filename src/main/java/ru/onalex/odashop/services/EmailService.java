package ru.onalex.odashop.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.onalex.odashop.dtos.CartItemDTO;


import java.util.List;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    // Фиксированные адреса поставщиков
    @Value("${app.mail.supplier.addresses}")
    private List<String> supplierEmails;

    public void sendOrderEmails(String customerEmail,
                                List<CartItemDTO> cartItems,
                                double total) {
        // Контекст для шаблона (общий для всех писем)
        Context context = new Context();
        context.setVariable("cartItems", cartItems);
        context.setVariable("total", total);

        // Генерация HTML содержимого
        String htmlContent = templateEngine.process("email/order-template", context);

        // 1. Отправка пользователю
        sendEmail(customerEmail, "Ваш заказ", htmlContent);

        // 2. Отправка всем поставщикам
        for (String supplierEmail : supplierEmails) {
            sendEmail(supplierEmail, "Новый заказ от клиента", htmlContent);
        }
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email to " + to, e);
        }
    }
}