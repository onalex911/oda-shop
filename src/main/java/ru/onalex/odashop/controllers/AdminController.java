package ru.onalex.odashop.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.onalex.odashop.entities.AdminUIUser;
import ru.onalex.odashop.services.ProductService;
import ru.onalex.odashop.services.AdminUIUserService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminUIUserService adminUIUserService;
    private final ProductService productService;

    public AdminController(AdminUIUserService adminUIUserService, ProductService productService) {
        this.adminUIUserService = adminUIUserService;
        this.productService = productService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("userCount", adminUIUserService.count());
        model.addAttribute("productCount", productService.count());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", adminUIUserService.findAll());
        return "admin/users";
    }

    @GetMapping("/tovar")
    public String products(Model model) {
        model.addAttribute("tovar", productService.findAll());
        return "admin/products";
    }

    // CRUD операции для пользователей
    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        AdminUIUser user = adminUIUserService.findById(id);
        model.addAttribute("user", user);
        return "admin/user-form";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") AdminUIUser user) {
        adminUIUserService.save(user);
        return "redirect:/admin/users";
    }

    // Аналогично для продуктов
}
