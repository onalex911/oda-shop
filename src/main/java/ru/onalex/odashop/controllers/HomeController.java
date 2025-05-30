package ru.onalex.odashop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
import ru.onalex.odashop.repositories.GrupTovRepository;

@Controller // временно замените на RestController для теста
@RequestMapping
public class HomeController {
    GrupTovRepository grupTovRepository;

    @Autowired
    public HomeController(GrupTovRepository grupTovRepository) {
        this.grupTovRepository = grupTovRepository;
    }

    @GetMapping("/account")
    public String getAccount(Model model) {
        model.addAttribute("grupTov", grupTovRepository.findAll());
        model.addAttribute("title","Вход/Регистрация");
        return "account";
    }

    @GetMapping("/contacts")
    public String getContacts(Model model) {
        model.addAttribute("grupTov", grupTovRepository.findAll());
        model.addAttribute("title","Контакты");
        return "contacts";
    }

    @GetMapping("/pravila")
    public String getPravila(Model model) {
        model.addAttribute("grupTov", grupTovRepository.findAll());
        model.addAttribute("title","Правила пользования настоящим сайтом");
        return "pravila";
    }

//    @GetMapping("/ping")
//    public String ping() {
//        return "pong";
//    }
}

