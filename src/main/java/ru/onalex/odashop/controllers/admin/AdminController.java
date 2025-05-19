package ru.onalex.odashop.controllers.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.onalex.odashop.services.CustomerService;
import ru.onalex.odashop.services.ProductService;

import java.security.Principal;

@Controller
@RequestMapping("/adminpanel")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

//    private final ProductService productService;
//    private final CustomerService customerService;
//
//    public AdminController(ProductService productService, CustomerService customerService) {
//        this.productService = productService;
//        this.customerService = customerService;
//    }

    @GetMapping
    public String adminPanel(Model model) {
        return "adminpanel/index";
    }

}
