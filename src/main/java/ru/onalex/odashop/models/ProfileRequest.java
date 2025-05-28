package ru.onalex.odashop.models;

import jakarta.persistence.Temporal;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.onalex.odashop.dtos.ProfileDTO;
import ru.onalex.odashop.dtos.TovarDTO;

import static ru.onalex.odashop.utils.ServiceUtils.replaceQuotes;

@Data
@Builder
public class ProfileRequest {
    //hidden
    private int customerId;

    @Size(max = 250, message = "Название организации может содержать не более 250 символов")
    private String orgName;

    @Size(max = 20, message = "ИНН может содержать не более 20 символов")
    private String inn;

    @Size(max = 250, message = "Адрес может содержать не более 250 символов")
    private String address;

    //@NotBlank(message = "Email не может быть пустым")
//    @Email(message = "Некорректный формат email")
//    private String email;

    @Size(max = 20, message = "Телефонный номер может содержать не более 20 символов")
    private String phone;

    @Size(max = 250, message = "Комментарий может содержать не более 250 символов")
    private String comment;

    public static ProfileRequest fromDTO(ProfileDTO dto) {
        return ProfileRequest.builder()
                .customerId(dto.getCustomerId())
                .orgName(dto.getOrgName())
                .inn(dto.getInn())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .comment(dto.getComment())
                .build();

    }

}