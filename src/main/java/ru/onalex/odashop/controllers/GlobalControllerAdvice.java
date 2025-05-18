package ru.onalex.odashop.controllers;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.onalex.odashop.dtos.CartItemDTO;
import ru.onalex.odashop.dtos.GrupTovDTO;
import ru.onalex.odashop.models.CartInfo;
import ru.onalex.odashop.repositories.CartItemRepository;
import ru.onalex.odashop.repositories.GrupTovRepository;
import ru.onalex.odashop.services.CartService;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
//@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final GrupTovRepository grupTovRepository;
    private CartService cartService;
    public static final String MAIN_PAGE = "/catalog/bizhuteriya";
    public static final String MAIN_ADMIN_PAGE = "/adminpanel";

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }
    // Внедрение через конструктор (рекомендуемый способ)
    public GlobalControllerAdvice(GrupTovRepository grupTovRepository) {
        this.grupTovRepository = grupTovRepository;
    }

    @ModelAttribute
    public void addAttributes(Model model, Authentication authentication, HttpSession session) {
        boolean isAuthenticated = (authentication != null && authentication.isAuthenticated());
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("title", "");
        model.addAttribute("mainPage", MAIN_PAGE);
        model.addAttribute("mainAdminPage", MAIN_ADMIN_PAGE);
        try {
            // Добавляем список групп товаров
            List<GrupTovDTO> groups = grupTovRepository.findBijou().stream()
                    .map(GrupTovDTO::fromEntity)
                    .collect(Collectors.toList());
            model.addAttribute("menuGroups", groups);

            model.addAttribute("cartInfo", new CartInfo(cartService.getCartItems(session)));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}