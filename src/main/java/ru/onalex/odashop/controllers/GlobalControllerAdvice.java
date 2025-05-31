package ru.onalex.odashop.controllers;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.onalex.odashop.dtos.GrupTovDTO;
import ru.onalex.odashop.models.CartInfo;
import ru.onalex.odashop.models.SortField;
import ru.onalex.odashop.models.CustomerData;
import ru.onalex.odashop.repositories.GrupTovRepository;
import ru.onalex.odashop.services.CartService;
import ru.onalex.odashop.services.CustomerService;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
//@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final GrupTovRepository grupTovRepository;
    private CartService cartService;
    private CustomerService customerService;
    public static boolean isAuthenticated;
    public static final String MAIN_PAGE = "/catalog/bizhuteriya";
    public static final String MAIN_ADMIN_PAGE = "/adminpanel";
    public static final List<SortField> SORT_FIELDS = List.of(
            new SortField("undef","—"),
            new SortField("price_up","цена возр."),
            new SortField("price_down","цена убыв."),
            new SortField("rem_up","остаток возр."),
            new SortField("rem_down","остаток убыв."),
            new SortField("dop","артикул")
    );
    public static final List<SortField> SEARCH_FIELDS = List.of(
            new SortField("tovName","наименов."),
            new SortField("tovName_","наименов. нач."),
            new SortField("dop","артикул сод."),
            new SortField("dop_","артикул нач."),
            new SortField("cena_ot","цена от..."),
            new SortField("cena_do","цена до..."),
            new SortField("ceny_ot_do","цена от-до")
    );

    @Autowired
    public void setCartService(CartService cartService, CustomerService customerService) {
        this.cartService = cartService;
        this.customerService = customerService;
    }
    // Внедрение через конструктор (рекомендуемый способ)
    public GlobalControllerAdvice(GrupTovRepository grupTovRepository) {
        this.grupTovRepository = grupTovRepository;
    }

    @ModelAttribute
    public void addAttributes(Model model, Authentication authentication, HttpSession session, Principal principal) {
        isAuthenticated = (authentication != null && authentication.isAuthenticated());
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        CustomerData customerData = isAuthenticated ? customerService.getUserInfoByUsername(principal.getName()) : null;

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("userData", customerData);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("title", "");
        model.addAttribute("mainPage", MAIN_PAGE);
        model.addAttribute("mainAdminPage", MAIN_ADMIN_PAGE);
        model.addAttribute("sortFields", SORT_FIELDS);
        model.addAttribute("searchFields", SEARCH_FIELDS);

        try {
            // Добавляем список групп товаров
            List<GrupTovDTO> groups = grupTovRepository.findBijouActive().stream()
                    .map(GrupTovDTO::fromEntity)
                    .collect(Collectors.toList());
            model.addAttribute("menuGroups", groups);

            double discount = customerData != null ? customerData.getCustomer().getDiscount() : 0.0;
            model.addAttribute("cartInfo",
                    new CartInfo(cartService.getCartItems(session),discount));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //для обработки несанкционированного досупа к админке
//    @ExceptionHandler(AccessDeniedException.class)
//    public String handleAccessDeniedException() {
//        return "redirect:/customer/account";
//    }
}