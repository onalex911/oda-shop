package ru.onalex.odashop.models;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderRequest {

    @NotBlank(message = "Контактное имя не должно быть пустым")
    private String contactName;

    @NotBlank(message = "Адрес не должен быть пустым")
    @Size(max = 250, message = "Адрес должен содержать не более 250 символов")
    private String address;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotNull
    @NotBlank(message = "Телефонный номер не может быть пустым")
    @Size(max = 20, message = "Телефонный номер должен содержать не более 20 символов")
    private String phone;

    @Size(max = 250, message = "Комментарий может содержать не более 250 символов")
    private String comment;

    @AssertTrue(message = "Необходимо согласие с условиями")
    private boolean termsAccepted;

    private long recvisitId;
    private boolean saveRecvisits;

    public OrderRequest(String contactName, String address, String email, String phone, String comment, long recvisitId) {
        this.contactName = contactName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.comment = comment;
        this.recvisitId = recvisitId;
        this.termsAccepted = false;
        this.saveRecvisits = true;
    }
}