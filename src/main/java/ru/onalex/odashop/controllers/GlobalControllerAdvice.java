package ru.onalex.odashop.controllers;


import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Calendar;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addAttributes(Model model, Authentication authentication) {
        boolean isAuthenticated = (authentication != null && authentication.isAuthenticated());
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("currentYear", currentYear);
    }
}