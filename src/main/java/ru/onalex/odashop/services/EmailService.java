package ru.onalex.odashop.services;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.onalex.odashop.dtos.CartItemDTO;
import ru.onalex.odashop.models.OrderRequest;
import ru.onalex.odashop.models.CustomerData;
import ru.onalex.odashop.models.RegisterRequest;


import java.util.List;

@Service
public class EmailService {

    @Value("${spring.mail.username}") String from;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final CartService cartService;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine, CartService cartService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.cartService = cartService;
    }

    // Фиксированные адреса поставщиков
    @Value("${app.mail.supplier.addresses}")
    private List<String> supplierEmails;

    public void sendOrderEmails(OrderRequest request, CustomerData customer,
                                List<CartItemDTO> cartItems,
                                HttpSession session) {
        String customerEmail = request.getEmail().isEmpty() ? customer.getCustomer().getUsername() : request.getEmail();
        // Контекст для шаблона (общий для всех писем)
        Context context = new Context();
        context.setVariable("cartItems", cartItems);
        context.setVariable("orderData", request);
        context.setVariable("userRecvisits", customer.getRecvisits().get(0));
        context.setVariable("totalSum", cartService.getTotalSum(session));
        context.setVariable("totalQuantity", cartService.getTotalQuantity(session));
        context.setVariable("discount", customer.getCustomer().getDiscount());

        // Генерация HTML содержимого
        String htmlContent = templateEngine.process("email/order", context);

        // 1. Отправка пользователю
//        System.out.println("we're sending to client: " + customerEmail);
        sendEmail(customerEmail, "Ваш заказ", htmlContent);

        // 2. Отправка всем поставщикам
        for (String supplierEmail : supplierEmails) {
//            System.out.println("we're sending to suppliers: " + supplierEmail);
            sendEmail(supplierEmail, "Новый заказ от клиента", htmlContent);
        }
    }

    public void sendRegEmail(RegisterRequest request) {
        String customerEmail = request.getUsername(); //он обязательно должен быть - его наличие проверяет контроллер
        // Контекст для шаблона (общий для всех писем)
        Context context = new Context();
        context.setVariable("username", request.getUsername());
        context.setVariable("contactName", request.getContactName());

        // Генерация HTML содержимого
        String htmlContentUser = templateEngine.process("email/register-user", context);
        String htmlContentAdmin = templateEngine.process("email/register-admin", context);

        // 1. Отправка пользователю
//        System.out.println("we're sending to client: " + customerEmail);
        sendEmail(customerEmail, "Вы зарегистрировались на сайте odadv.ru", htmlContentUser);

        // 2. Отправка всем поставщикам
        for (String supplierEmail : supplierEmails) {
//            System.out.println("we're sending to suppliers: " + supplierEmail);
            sendEmail(supplierEmail, "Новая регистрация на сайте odadv.ru", htmlContentAdmin);
        }
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom("onalex911@yandex.ru");  // Явно указываем отправителя
//            message.setTo(to);     // Проверенный email получателя
//            message.setSubject("Test SMTP");
//            message.setText("This is a test email.");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed (I) to send email to " + to, e);
        } catch (Exception e) {
            //row new Exception("Failed to send email to " + to, e);
            throw new RuntimeException("Failed (II) to send email to " + to + ": " + e.getMessage(), e);
        }
    }
}