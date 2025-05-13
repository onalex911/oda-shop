package ru.onalex.odashop.services;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import ru.onalex.odashop.entities.Tovar;
import ru.onalex.odashop.entities.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    public List<CartItem> getCartItems(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    public void addToCart(HttpSession session, Tovar tovar, int quantity) {
        List<CartItem> cart = getCartItems(session);

        // Проверяем, есть ли товар уже в корзине
        Optional<CartItem> existingItem = cart.stream()
                .filter(item -> item.getTovar().getId() == tovar.getId())
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            cart.add(new CartItem(tovar, quantity));
        }
    }

    public void removeFromCart(HttpSession session, Long tovarId) {
        List<CartItem> cart = getCartItems(session);
        cart.removeIf(item -> item.getTovar().getId() == tovarId);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute("cart");
    }

    public double getTotal(HttpSession session) {
        return getCartItems(session).stream()
                .mapToDouble(item -> item.getTovar().getCena() * item.getQuantity())
                .sum();
    }
}
