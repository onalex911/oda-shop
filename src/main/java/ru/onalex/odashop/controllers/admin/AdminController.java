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

    private final ProductService productService;
    private final CustomerService customerService;

    public AdminController(ProductService productService, CustomerService customerService) {
        this.productService = productService;
        this.customerService = customerService;
    }

    @GetMapping
//    public String dashboard(Model model) {
    public String adminPanel(Principal principal, Model model) {
        model.addAttribute("userData", customerService.getUserInfoByUsername(principal.getName()));
//        model.addAttribute("productCount", productService.count());
        return "adminpanel/index";
    }

//    @GetMapping("/users")
//    public String users(Model model) {
//        model.addAttribute("users", adminUIUserService.findAll());
//        return "admin/users";
//    }
//
//    @GetMapping("/tovar")
//    public String products(Model model) {
//        model.addAttribute("tovar", productService.findAll());
//        return "admin/products";
//    }
//
//    // CRUD операции для пользователей
//    @GetMapping("/users/edit/{id}")
//    public String editUser(@PathVariable Long id, Model model) {
//        AdminUIUser user = adminUIUserService.findById(id);
//        model.addAttribute("user", user);
//        return "admin/user-form";
//    }
//
//    @PostMapping("/users/save")
//    public String saveUser(@ModelAttribute("user") AdminUIUser user) {
//        adminUIUserService.save(user);
//        return "redirect:/admin/users";
//    }

}
