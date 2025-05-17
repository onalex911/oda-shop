package ru.onalex.odashop.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.onalex.odashop.dtos.CartItemDTO;
import ru.onalex.odashop.entities.Customer;
import ru.onalex.odashop.services.CartService;
import ru.onalex.odashop.services.CustomerService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

//    private CartService cartService;
//
//    @Autowired
//    public void setCartService(CartService cartService) {
//        this.cartService = cartService;
//    }

    private CustomerService customerService;

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String customer(Principal principal, Model model) {
//    public String customer(Principal principal) {
        Customer customer = customerService.findByUsername(principal.getName());
        model.addAttribute("customer", customer);
        return "customer";
    }

//    @GetMapping("/checkout")
//    public String doCheckout(Model model, HttpSession session) {
//
//        List<CartItemDTO> items = cartService.getCartItems(session);
//        double total = cartService.getTotal(session);
//        if (total > 0 && items.size() > 0) {
//            model.addAttribute("cartItems", items);
//            model.addAttribute("total", total);
//            return "checkout";
//        }
//        return "checkout-empty";
//    }

//    @GetMapping("/login")
//    public String doLogin(Model model) {
//        return "account";
//    }
}
