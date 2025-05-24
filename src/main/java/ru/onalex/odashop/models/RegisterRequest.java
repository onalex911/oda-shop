package ru.onalex.odashop.models;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String contactName;
    private String password;
}