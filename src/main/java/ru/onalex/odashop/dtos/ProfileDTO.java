package ru.onalex.odashop.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.onalex.odashop.models.ProfileRequest;

import static ru.onalex.odashop.utils.ServiceUtils.replaceQuotes;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    private int customerId;
    private String orgName;             // Название организации
    private String inn;                 // ИНН
    private String address;             // Адрес
    private String phone;               // Телефон
    private String comment;

//    public static ProfileDTO toDTO(ProfileRequest request) {
//        return ProfileDTO.builder()
//                .customerId(request.getCustomerId())
//                .orgName(request.getOrgName())
//                .inn(request.getInn())
//                .address(request.getAddress())
//                .phone(request.getPhone())
//                .comment(request.getComment())
//                .build();
//    }
}