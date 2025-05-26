package ru.onalex.odashop.services;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.onalex.odashop.dtos.CartItemDTO;
import ru.onalex.odashop.dtos.TovarDTO;
import ru.onalex.odashop.entities.Tovar;
import ru.onalex.odashop.models.CartInfo;
import ru.onalex.odashop.models.CustomerData;
import ru.onalex.odashop.repositories.TovarRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.onalex.odashop.controllers.GlobalControllerAdvice.isAuthenticated;

@Service
@RequiredArgsConstructor
public class CartService {
    private final TovarRepository tovarRepository;
    private final ImageService imageService;
    private final CustomerService customerService;

//    public CartService(TovarRepository tovarRepository, ImageService imageService,CustomerService customerService) {
//        this.tovarRepository = tovarRepository;
//        this.imageService = imageService;
//        this.customerService = customerService;
//    }

    public String vewCart(Model model, HttpSession session, Principal principal) {
        List<CartItemDTO> items = getCartItems(session);
        CartInfo cartInfo = new CartInfo(items,getDiscountByUser(principal));
//        double totalSum = getTotalSum(session);
        double totalSum = cartInfo.getTotalSum();
//        int totalQuantity = getTotalQuantity(session);
        int totalQuantity = cartInfo.getTotalQuantity();
        double totalSumDisc = cartInfo.getTotalSumDisc();
        if (totalSum > 0 && items.size() > 0) {
            model.addAttribute("cartItems", items);
            model.addAttribute("totalSum", totalSum);
            model.addAttribute("totalSumDisc", totalSumDisc);
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

    public List<CartItemDTO> refreshCart(HttpSession session, TovarDTO tovar, int quantity) {
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
        return cart;
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

    public double getTotalSum(HttpSession session) {
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

    public CartInfo addToCartQuiet(Long id, int quantity, HttpSession session, Principal principal) {
        Tovar tovar = tovarRepository.findById(Math.toIntExact(id)).orElseThrow();
        TovarDTO tovarDTO = TovarDTO.fromEntity(tovar);

        tovarDTO.setRealPreview(imageService.getImagePath(tovar.getPicPreview()));
        tovarDTO.setRealPicBig(imageService.getImagePath(tovar.getPicBig()));

//        System.out.println("pic preview: " + tovar.getPicPreview());

        addToCart(session, tovarDTO, quantity);
        return new CartInfo(getCartItems(session),getDiscountByUser(principal));
    }

    public CartInfo refreshCartContr(Long id, int quantity, HttpSession session,Principal principal) {
        Tovar tovar = tovarRepository.findById(Math.toIntExact(id)).orElseThrow();
        List<CartItemDTO> cart = refreshCart(session, TovarDTO.fromEntity(tovar), quantity);
        System.out.println("Refreshed id: " + id + " new quantity: " + quantity);
        CartInfo cartInfo = new CartInfo(getCartItems(session),getDiscountByUser(principal));
        cartInfo.setSumPos(cart.stream() .filter(t -> t.getTovar().getId() == id) // Фильтруем по id
                .mapToDouble(c -> c.getTovar().getCena()) // Получаем цену товара
                .findFirst() // Находим первый (и единственный) элемент
                .orElse(0.0) // Если товар не найден, возвращаем 0.0
                * quantity); // Умножаем на количество
        return cartInfo;
//        return "redirect:/cart";
    }

    public double getDiscountByUser(Principal principal) {
        CustomerData customerData = isAuthenticated ? customerService.getUserInfoByUsername(principal.getName()) : null;
        return customerData != null ? customerData.getCustomer().getDiscount() : 0.0;
//        return 0.0;
    }
}
