package ru.onalex.odashop.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.onalex.odashop.entities.Customer;
import ru.onalex.odashop.repositories.CustomerRepository;
import ru.onalex.odashop.services.CustomerService;
import ru.onalex.odashop.services.ProductService;

import java.security.Principal;

@Controller
@RequestMapping("/adminpanel")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;

//    public AdminController(CustomerService customerService) {
//        this.customerService = customerService;
//    }

    @GetMapping
    public String adminPanel(Principal principal, Model model) {
        System.out.println("Principal: " + principal.getName());
//        Customer customer = customerService.findByUsername(principal.getName());
        Customer customer = customerRepository.findByUsernameWithRoles(principal.getName());
        System.out.println(customer.toString());
        // Дополнительная проверка на случай, если аннотация @PreAuthorize не сработает
        boolean isAdmin = customer.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        System.out.println("isAdmin = " + isAdmin);

        if (!isAdmin) {
            return "redirect:/account";
        }
        model.addAttribute("error_message", "It's ok!");
        return "adminpanel/index";
    }

}
