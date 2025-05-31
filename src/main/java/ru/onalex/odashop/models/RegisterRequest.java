package ru.onalex.odashop.models;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    @Size(max = 250, message = "Email может содержать не более 250 символов")
    private String username;

    @NotBlank(message = "Контактное имя не должно быть пустым")
    @Size(max = 250, message = "Контактное имя может содержать не более 250 символов")
    private String contactName;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, max=250, message = "Пароль должен содержать минимум 6, максимум 250 символов")
    private String password;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, max=250, message = "Пароль 2 должен содержать минимум 6, максимум 250 символов")
    private String password2;

    @AssertTrue(message = "Необходимо согласие с условиями")
    private boolean termsAccepted;

}