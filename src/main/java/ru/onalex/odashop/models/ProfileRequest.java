package ru.onalex.odashop.models;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileRequest {
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    @Size(max = 250, message = "Email может содержать не более 250 символов")
    private String username;
	
    @Size(max = 250, message = "Название организации может содержать не более 250 символов")
    private String organization;

    @NotBlank(message = "Контактное имя не должно быть пустым")
    @Size(max = 250, message = "Контактное имя может содержать не более 250 символов")
    private String contactName;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, max=250, message = "Пароль должен содержать минимум 6, максимум 250 символов")
    private String password;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, max=250, message = "Пароль 2 должен содержать минимум 6, максимум 250 символов")
    private String password2;

    @Size(max = 250, message = "Адрес может содержать не более 250 символов")
    private String address;

    //@NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @Size(max = 20, message = "Телефонный номер может содержать не более 20 символов")
    private String phone;

    @Size(max = 250, message = "Комментарий может содержать не более 250 символов")
    private String comment;

}