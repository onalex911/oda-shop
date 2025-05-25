package ru.onalex.odashop.models;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderRequest {

    //@NotBlank(message = "Контактное имя не должно быть пустым")
    private String contactName;

    @Size(max = 250, message = "Адрес должен содержать не более 250 символов")
    private String address;

    //@NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @Size(max = 20, message = "Телефонный номер должен содержать не более 20 символов")
    private String phone;

    @AssertTrue(message = "Необходимо согласие с условиями")
    private boolean termsAccepted;

    public OrderRequest(String contactName, String address, String email, String phone) {
        this.contactName = contactName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.termsAccepted = false;
    }
}