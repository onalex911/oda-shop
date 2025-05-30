package ru.onalex.odashop.models;

import jakarta.persistence.Temporal;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.onalex.odashop.dtos.ProfileDTO;
import ru.onalex.odashop.dtos.TovarDTO;

import static ru.onalex.odashop.utils.ServiceUtils.replaceQuotes;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    //hidden
    private int customerId;
    private long recvisitId;
    private double discount;

//    @Size(max = 250, message = "Название организации может содержать не более 250 символов")
//    private String orgName;
//
//    @Size(max = 20, message = "ИНН может содержать не более 20 символов")
//    private String inn;
    @NotBlank(message = "Почтовый адрес не может быть пустым")
    @Size(max = 250, message = "Почтовый адрес может содержать не более 250 символов")
    private String address;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String username;

    @NotBlank(message = "Контактное имя не может быть пустым")
    private String contactName;

    @NotBlank(message = "Телефонный номер не может быть пустым")
    @Size(max = 20, message = "Телефонный номер может содержать не более 20 символов")
    private String phone;

    @Size(max = 250, message = "Комментарий может содержать не более 250 символов")
    private String comment;



//    public static ProfileRequest fromDTO(ProfileDTO dto) {
//        return ProfileRequest.builder()
//                .customerId(dto.getCustomerId())
//                .orgName(dto.getOrgName())
//                .inn(dto.getInn())
//                .address(dto.getAddress())
//                .phone(dto.getPhone())
//                .comment(dto.getComment())
//                .build();
//
//    }

}