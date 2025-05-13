package ru.onalex.odashop.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.onalex.odashop.entities.CartItem;
import ru.onalex.odashop.entities.Tovar;
import ru.onalex.odashop.repositories.TovarRepository;
import ru.onalex.odashop.services.CartService;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private TovarRepository tovarRepository;

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        List<CartItem> items = cartService.getCartItems(session);
        double total = cartService.getTotal(session);
        if (total > 0 && items.size() > 0) {
            model.addAttribute("cartItems", items);
            model.addAttribute("total", total);
            model.addAttribute("title", "Корзина. ");
            return "cart";
        }
        model.addAttribute("title", "Корзина пуста. ");
        return "cart-empty";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session) {
        Tovar product = tovarRepository.findById(Math.toIntExact(productId)).orElseThrow();
        cartService.addToCart(session, product, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long productId, HttpSession session) {
        cartService.removeFromCart(session, productId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        cartService.clearCart(session);
        return "redirect:/cart";
    }
}
