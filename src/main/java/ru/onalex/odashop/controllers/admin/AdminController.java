package ru.onalex.odashop.controllers.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.onalex.odashop.entities.Customer;
import ru.onalex.odashop.services.CustomerService;
import ru.onalex.odashop.services.ProductService;

import java.security.Principal;

@Controller
@RequestMapping("/adminpanel")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final CustomerService customerService;

    public AdminController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String adminPanel(Principal principal, Model model) {

        Customer customer = customerService.findByUsername(principal.getName());

        // Дополнительная проверка на случай, если аннотация @PreAuthorize не сработает
        boolean isAdmin = customer.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        if (!isAdmin) {
            return "redirect:/account";
        }
        model.addAttribute("error_message", "It's ok!");
        return "adminpanel/index";
    }

}
