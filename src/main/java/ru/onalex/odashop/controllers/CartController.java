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
        List<CartItemDTO> items = cartService.getCartItems(session);
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
    public String addToCart(@RequestParam Long tovarId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session) {
        Tovar tovar = tovarRepository.findById(Math.toIntExact(tovarId)).orElseThrow();
        cartService.addToCart(session, TovarDTO.fromEntity(tovar), quantity);
        return "redirect:/cart";
    }

    @PostMapping("/add/{id}/{quantity}")
    @ResponseBody
    public CartInfo addToCartQuiet(@PathVariable Long id,
                                   @PathVariable int quantity,
                                   HttpSession session) {
        Tovar tovar = tovarRepository.findById(Math.toIntExact(id)).orElseThrow();
        cartService.addToCart(session, TovarDTO.fromEntity(tovar), quantity);
        return new CartInfo(cartService.getCartItems(session));
    }

    @PostMapping("/refresh/{id}/{quantity}")
    public void refreshCart(@PathVariable Long id,
                            @PathVariable int quantity,
                            HttpSession session) {
        Tovar tovar = tovarRepository.findById(Math.toIntExact(id)).orElseThrow();
        cartService.refreshCart(session, TovarDTO.fromEntity(tovar), quantity);
//        System.out.println("Refreshed id: " + id + " new quantity: " + quantity);
//        return "redirect:/cart";
    }


    @DeleteMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        System.out.println("removing: "+id);
        cartService.removeFromCart(session, id);
        if(cartService.getCartItems(session).size() == 0) {
            return "cart-empty";
        }else{
            return "";
        }
    }

    @PostMapping("/clear")
    public String clearCart(Model model, HttpSession session) {
        cartService.clearCart(session);
        System.out.println("Cart size: " + cartService.getCartItems(session).size());
        model.addAttribute("title", "Корзина пуста. ");
        return "cart-empty";

    }
}
