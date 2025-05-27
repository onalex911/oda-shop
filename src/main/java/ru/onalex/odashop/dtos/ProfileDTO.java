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
    private String username;
    private String contactName;
    private Double discount;
    private String orgName;             // poluchatel
    private String headName;           // rukovoditel
    private String address;            // postavshik_adres
    private String phone;              // postavshik_telefon
    private String comment;

    public static ProfileDTO toDTO(ProfileRequest request) {
        return ProfileDTO.builder()
                .customerId(request.getCustomerId())
                .username(request.getUsername())
                .contactName(request.getContactName())
                .discount(request.getDiscount())
                .orgName(request.getOrgName())
                .headName(request.getHeadName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .comment(request.getComment())
                .build();
    }
}