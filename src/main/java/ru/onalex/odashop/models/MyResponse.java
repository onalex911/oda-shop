package ru.onalex.odashop.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
public class MyResponse<T> {
    public static final String SUCCESS = "success";
    private String status; // "success" или "error"
    private String message;
    private List<T> dataset;

    // Статические фабричные методы для создания ответов
    public static MyResponse success() {
        return new MyResponse(SUCCESS, "",null);
    }

    public static MyResponse success(String message) {
        return new MyResponse(SUCCESS, message,null);
    }

    public static MyResponse error(String message) {
        return new MyResponse("error", message,null);
    }

    public MyResponse(List<T> dataset) {
        this.status = SUCCESS;
        this.message = "";
        this.dataset = dataset;
    }
}