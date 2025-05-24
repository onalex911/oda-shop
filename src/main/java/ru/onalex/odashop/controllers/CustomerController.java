package ru.onalex.odashop.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.onalex.odashop.dtos.CartItemDTO;
import ru.onalex.odashop.dtos.RecvisitDTO;
import ru.onalex.odashop.entities.Customer;
import ru.onalex.odashop.entities.Recvisit;
import ru.onalex.odashop.models.OrderRequest;
import ru.onalex.odashop.models.RegisterRequest;
import ru.onalex.odashop.models.UserInfo;
import ru.onalex.odashop.services.CartService;
import ru.onalex.odashop.services.CustomerService;
import ru.onalex.odashop.services.EmailService;

import java.security.Principal;
import java.util.List;

import static ru.onalex.odashop.controllers.GlobalControllerAdvice.MAIN_PAGE;
import static ru.onalex.odashop.utils.ServiceUtils.replaceQuotes;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final CartService cartService;
    private final CustomerService customerService;

    public CustomerController(CartService cartService,
                              CustomerService customerService) {
        this.cartService = cartService;
        this.customerService = customerService;

    }

    @GetMapping
    public String customer(Principal principal, Model model) {
//    public String customer(Principal principal) {
        Customer customer = customerService.findByUsername(principal.getName());
        model.addAttribute("customer", customer);
        return "customer";
    }

    @GetMapping("/checkout")
    public String doCheckout(Model model, HttpSession session, Principal principal) {

        List<CartItemDTO> items = cartService.getCartItems(session);
        double totalSum = cartService.getTotal(session);
        if (totalSum > 0 && items.size() > 0) {
//            Customer customer = customerService.findByUsername(principal.getName());
            String address = "";
            String phone = "";
            String email = "";
            String contactName = "";
            String comment = "";
            double discount = 0;
            UserInfo userInfo = customerService.getUserInfoByUsername(principal.getName());
            if(!userInfo.getRecvisits().isEmpty()) {
                Recvisit recvisit = userInfo.getRecvisits().get(0);
                address = recvisit.getCustomerAddress();
                phone = recvisit.getCustomerPhone();
                email = userInfo.getCustomer().getUsername();
                comment = recvisit.getComment();
                contactName = userInfo.getCustomer().getContactName();
                discount = userInfo.getCustomer().getDiscount();
            }
            model.addAttribute("cartItems", items);
            model.addAttribute("totalSum", totalSum);
            model.addAttribute("discount", discount);
            model.addAttribute("totalSumDisc", totalSum * (100 - discount) / 100);
            model.addAttribute("orderRequest", new OrderRequest(contactName,replaceQuotes(address),email,phone,comment));

            return "checkout";
        }
        return "checkout-empty";
    }

    @GetMapping("/login")
    public String doLogin(Model model) {
        // Добавляем пустой объект для формы регистрации
        model.addAttribute("registerRequest", new RegisterRequest());
        return "account";
    }

    @PostMapping("/register")
    public String doRegistration(@Valid @ModelAttribute("registerRequest") RegisterRequest request,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            // Возвращаем тот же шаблон, где есть форма
            return "account";
        }

        try {
            customerService.doRegistration(request);
            return "redirect:/customer/login?success";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "account";
        }
    }

    @PostMapping("/order")
    public String doOrder(@Valid @ModelAttribute("orderRequest") OrderRequest request,
                            BindingResult bindingResult,
                          Principal principal,
                          HttpSession session,
                            Model model) {
        if (bindingResult.hasErrors()) {
            // Возвращаем тот же шаблон, где есть форма
            return "checkout";
        }

        try {
            Customer customer = customerService.findByUsername(principal.getName());
            customerService.doOrder(request,customer,session,model);
            return "redirect:/customer/order?success";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "checkout";
        }
//        return "redirect:" + MAIN_PAGE;
    }


    @GetMapping("/order")
    public String successOrderPage(
            @RequestParam(value = "success", required = false) String success,
            Model model) {
        if (success != null) {
            // Обработка успешного заказа
            model.addAttribute("successMessage", "Ваш заказ успешно оформлен!");
            return "checkout-success"; // Имя шаблона представления
        }

        // Логика для обычной страницы заказа
        return "checkout";
    }

}
