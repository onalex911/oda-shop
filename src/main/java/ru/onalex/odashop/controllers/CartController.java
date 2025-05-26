package ru.onalex.odashop.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.onalex.odashop.models.CartInfo;
import ru.onalex.odashop.models.CustomerData;
import ru.onalex.odashop.services.CartService;
import ru.onalex.odashop.services.CustomerService;

import java.security.Principal;

import static ru.onalex.odashop.controllers.GlobalControllerAdvice.isAuthenticated;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CustomerService customerService;

//    public CartController(CartService cartService, CustomerService customerService) {
//        this.cartService = cartService;
//        this.customerService = customerService;
//    }

    @GetMapping
    public String viewCart(Model model, HttpSession session,Principal principal) {
        return cartService.vewCart(model,session,principal);
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long tovarId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session) {
        return cartService.addToCartContr(tovarId, quantity, session);
    }

    @PostMapping("/add/{id}/{quantity}")
    @ResponseBody
    public CartInfo addToCartQuiet(@PathVariable Long id,
                                   @PathVariable int quantity,
                                   HttpSession session, Principal principal) {
        return cartService.addToCartQuiet(id,quantity,session,principal);
    }

    @ResponseBody
    @PostMapping("/refresh/{id}/{quantity}")
    public CartInfo refreshCart(@PathVariable Long id,
                            @PathVariable int quantity,
                            HttpSession session, Principal principal) {

       return cartService.refreshCartContr(id,quantity,session,principal);
    }

    @DeleteMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session, Principal principal) {
//        System.out.println("removing: "+id);
        return cartService.removeFromCartContr(id,session,principal);
    }

    @PostMapping("/clear")
    public String clearCart(Model model, HttpSession session) {
        return cartService.clearCartContr(model,session);
    }
}
