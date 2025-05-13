package ru.onalex.odashop.controllers;


import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.onalex.odashop.dtos.GrupTovDTO;
import ru.onalex.odashop.repositories.GrupTovRepository;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final GrupTovRepository grupTovRepository;

    // Внедрение через конструктор (рекомендуемый способ)
    public GlobalControllerAdvice(GrupTovRepository grupTovRepository) {
        this.grupTovRepository = grupTovRepository;
    }

    @ModelAttribute
    public void addAttributes(Model model, Authentication authentication) {
        boolean isAuthenticated = (authentication != null && authentication.isAuthenticated());
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("title", "");
        try {
            // Добавляем список групп товаров
            List<GrupTovDTO> groups = grupTovRepository.findBijou().stream()
                    .map(GrupTovDTO::fromEntity)
                    .collect(Collectors.toList());
            model.addAttribute("menuGroups", groups);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}