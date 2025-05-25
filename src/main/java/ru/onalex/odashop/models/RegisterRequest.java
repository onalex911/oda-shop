package ru.onalex.odashop.models;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String username;

    @NotBlank(message = "Контактное имя не должно быть пустым")
    private String contactName;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;

    @AssertTrue(message = "Необходимо согласие с условиями")
    private boolean termsAccepted;

}