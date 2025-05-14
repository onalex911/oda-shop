package ru.onalex.odashop.services;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import ru.onalex.odashop.dtos.CartItemDTO;
import ru.onalex.odashop.dtos.TovarDTO;
import ru.onalex.odashop.entities.Tovar;
import ru.onalex.odashop.entities.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    public List<CartItemDTO> getCartItems(HttpSession session) {

        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
//                .stream()
//                .map(c -> new CartItemDTO(TovarDTO.fromEntity(c.getTovar()),c.getQuantity()))
//                .toList();
    }

    public void addToCart(HttpSession session, TovarDTO tovar, int quantity) {
        List<CartItemDTO> cart = getCartItems(session);

        // Проверяем, есть ли товар уже в корзине
        Optional<CartItemDTO> existingItem = cart.stream()
                .filter(item -> item.getTovar().getId() == tovar.getId())
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            cart.add(new CartItemDTO(tovar, quantity));
        }
    }
    public void refreshCart(HttpSession session, TovarDTO tovar, int quantity) {
        List<CartItemDTO> cart = getCartItems(session);

        // Проверяем, есть ли товар уже в корзине
        Optional<CartItemDTO> existingItem = cart.stream()
                .filter(item -> item.getTovar().getId() == tovar.getId())
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(quantity);
        } else {
            cart.add(new CartItemDTO(tovar, quantity));
        }
    }

    public void removeFromCart(HttpSession session, Long tovarId) {
        List<CartItemDTO> cart = getCartItems(session);
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
