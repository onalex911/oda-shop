package ru.onalex.odashop.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // временно замените на RestController для теста
@RequestMapping("/test")
public class TestController {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}

