package ru.onalex.odashop.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyResponse {
    private String status; // "success" или "error"
    private String message;

    // Статические фабричные методы для создания ответов
    public static MyResponse success() {
        return new MyResponse("success", "");
    }

    public static MyResponse success(String message) {
        return new MyResponse("success", message);
    }

    public static MyResponse error(String message) {
        return new MyResponse("error", message);
    }
}