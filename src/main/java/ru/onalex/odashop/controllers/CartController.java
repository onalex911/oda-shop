package ru.onalex.odashop.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.onalex.odashop.dtos.CartItemDTO;
import ru.onalex.odashop.dtos.TovarDTO;
import ru.onalex.odashop.entities.CartItem;
import ru.onalex.odashop.entities.Tovar;
import ru.onalex.odashop.models.CartInfo;
import ru.onalex.odashop.repositories.TovarRepository;
import ru.onalex.odashop.services.CartService;
import ru.onalex.odashop.services.ImageService;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        return cartService.vewCart(model,session);
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
                                   HttpSession session) {
        return cartService.addToCartQuiet(id,quantity,session);
    }

    @PostMapping("/refresh/{id}/{quantity}")
    public void refreshCart(@PathVariable Long id,
                            @PathVariable int quantity,
                            HttpSession session) {
       cartService.refreshCartContr(id,quantity,session);
    }

    @DeleteMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
//        System.out.println("removing: "+id);
        return cartService.removeFromCartContr(id,session);
    }

    @PostMapping("/clear")
    public String clearCart(Model model, HttpSession session) {
        return cartService.clearCartContr(model,session);
    }
}
