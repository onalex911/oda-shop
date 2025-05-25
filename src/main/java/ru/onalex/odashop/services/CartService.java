package ru.onalex.odashop.services;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.onalex.odashop.dtos.CartItemDTO;
import ru.onalex.odashop.dtos.TovarDTO;
import ru.onalex.odashop.entities.Tovar;
import ru.onalex.odashop.entities.CartItem;
import ru.onalex.odashop.models.CartInfo;
import ru.onalex.odashop.repositories.CartRepository;
import ru.onalex.odashop.repositories.TovarRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final TovarRepository tovarRepository;
    private final ImageService imageService;

    public CartService(TovarRepository tovarRepository, ImageService imageService) {
        this.tovarRepository = tovarRepository;
        this.imageService = imageService;
    }

    public String vewCart(Model model, HttpSession session) {
        List<CartItemDTO> items = getCartItems(session);
        double totalSum = getTotal(session);
        int totalQuantity = getTotalQuantity(session);
        if (totalSum > 0 && items.size() > 0) {
            model.addAttribute("cartItems", items);
            model.addAttribute("totalSum", totalSum);
            model.addAttribute("totalQuantity", totalQuantity);
            model.addAttribute("title", "Корзина. ");
            return "cart";
        }
        model.addAttribute("title", "Корзина пуста. ");
        return "cart-empty";
    }

    public List<CartItemDTO> getCartItems(HttpSession session) {

        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    public String addToCartContr(Long tovarId, int quantity, HttpSession session) {
        Tovar tovar = tovarRepository.findById(Math.toIntExact(tovarId)).orElseThrow();
        TovarDTO tovarDTO = TovarDTO.fromEntity(tovar);

        tovarDTO.setRealPreview(imageService.getImagePath(tovar.getPicPreview()));
        tovarDTO.setRealPicBig(imageService.getImagePath(tovar.getPicBig()));

//        System.out.println("pic preview: " + tovar.getPicPreview());
        addToCart(session, tovarDTO, quantity);
        return "redirect:/cart";
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

    public String removeFromCartContr(Long id, HttpSession session) {
        removeFromCart(session, id);
        if(getCartItems(session).isEmpty()) {
            return "cart-empty";
        }else{
            return "";
        }
    }

    public void removeFromCart(HttpSession session, Long tovarId) {
        List<CartItemDTO> cart = getCartItems(session);
        cart.removeIf(item -> item.getTovar().getId() == tovarId);
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

    public String clearCartContr(Model model,HttpSession session) {
        clearCart(session);
//        System.out.println("Cart size: " + getCartItems(session).size());
        model.addAttribute("title", "Корзина пуста. ");
        return "cart-empty";
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute("cart");
    }

    public double getTotal(HttpSession session) {
        return getCartItems(session).stream()
                .mapToDouble(item -> item.getTovar().getCena() * item.getQuantity())
                .sum();
    }

    public int getQuantity(int id, HttpSession session) {
        return getCartItems(session).stream().mapToInt(item -> item.getTovar().getId() == id ? item.getQuantity() : 0).sum();
    }

    public int getTotalQuantity(HttpSession session) {
        return getCartItems(session).stream().mapToInt(item -> item.getQuantity()).sum();
    }

    public CartInfo addToCartQuiet(Long id, int quantity, HttpSession session) {
        Tovar tovar = tovarRepository.findById(Math.toIntExact(id)).orElseThrow();
        TovarDTO tovarDTO = TovarDTO.fromEntity(tovar);

        tovarDTO.setRealPreview(imageService.getImagePath(tovar.getPicPreview()));
        tovarDTO.setRealPicBig(imageService.getImagePath(tovar.getPicBig()));

//        System.out.println("pic preview: " + tovar.getPicPreview());

        addToCart(session, tovarDTO, quantity);
        return new CartInfo(getCartItems(session));
    }

    public void refreshCartContr(Long id, int quantity, HttpSession session) {
        Tovar tovar = tovarRepository.findById(Math.toIntExact(id)).orElseThrow();
        refreshCart(session, TovarDTO.fromEntity(tovar), quantity);
//        System.out.println("Refreshed id: " + id + " new quantity: " + quantity);
//        return "redirect:/cart";
    }
}
