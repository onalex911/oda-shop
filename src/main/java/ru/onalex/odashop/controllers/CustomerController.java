package ru.onalex.odashop.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.onalex.odashop.dtos.CartItemDTO;
import ru.onalex.odashop.entities.Customer;
import ru.onalex.odashop.entities.Recvisit;
import ru.onalex.odashop.models.*;
import ru.onalex.odashop.repositories.CustomerRepository;
import ru.onalex.odashop.repositories.RecvisitRepository;
import ru.onalex.odashop.services.CartService;
import ru.onalex.odashop.services.CustomerService;
import ru.onalex.odashop.services.EmailService;

import java.security.Principal;
import java.util.List;

import static ru.onalex.odashop.utils.ServiceUtils.getSumWithDiscount;
import static ru.onalex.odashop.utils.ServiceUtils.replaceQuotes;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CartService cartService;
    private final CustomerService customerService;
    private final EmailService emailService;
    private final RecvisitRepository recvisitRepository;
    private final CustomerRepository customerRepository;

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
        double totalSum = cartService.getTotalSum(session);
        if (totalSum > 0 && items.size() > 0) {
//            Customer customer = customerService.findByUsername(principal.getName());
            String address = "";
            String phone = "";
            String email = "";
            String contactName = "";
            String comment = "";
            double discount = 0;
            long recvisitId = 0;
            CustomerData userInfo = customerService.getUserInfoByUsername(principal.getName());
            if (!userInfo.getRecvisits().isEmpty()) {
                Recvisit recvisit = userInfo.getRecvisits().get(0);
                address = recvisit.getCustomerAddress();
                phone = recvisit.getCustomerPhone();
                email = userInfo.getCustomer().getUsername();
                comment = recvisit.getComment();
                contactName = userInfo.getCustomer().getContactName();
                discount = userInfo.getCustomer().getDiscount();
                recvisitId = recvisit.getId();
            }
            model.addAttribute("cartItems", items);
            model.addAttribute("totalSum", totalSum);
            model.addAttribute("discount", discount);
            model.addAttribute("totalSumDisc", getSumWithDiscount(totalSum, discount));
            model.addAttribute("orderRequest", new OrderRequest(contactName, replaceQuotes(address), email, phone, comment, recvisitId));

            return "checkout";
        }
        return "checkout-empty";
    }

    @GetMapping("/login")
    public String doLogin(@RequestParam(required = false) String success,
                          Model model) {
        if (success != null) {
            model.addAttribute("success", "Вы успешно зарегистрированы! " +
                    "Для оформления заказа, пожалуйста, введите e-mail и пароль в блоке «Вход»");
        }
        // Добавляем пустой объект для формы регистрации
        model.addAttribute("registerRequest", new RegisterRequest());
//        model.addAttribute("session", session);
        return "account";
    }

    @PostMapping("/register")
    public String doRegistration(@Valid @ModelAttribute("registerRequest") RegisterRequest request,
                                 BindingResult bindingResult,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            // Возвращаем тот же шаблон, где есть форма

            model.addAttribute("errorMessage", "Введены неверные данные для регистрации!");
            return "account";
        }

        try {
            customerService.doRegistration(request, null);
            // Отправляем письма (пользователю + поставщикам)
//            System.out.println("Attempt to send email to:" + request.getUsername());
            emailService.sendRegEmail(request);
            return "redirect:/customer/login?success";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "account";
        }
    }

    @GetMapping("/profile")
    public String openProfile(Model model, Principal principal) {

        model.addAttribute("title", "Профиль");
        try {
            model.addAttribute("profileRequest", customerService.getProfileRequest(principal.getName()));
        } catch (Exception e) {

            model.addAttribute("profileRequest", new RegisterRequest());
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "profile";
    }

    @PostMapping("/profile")
    public String editProfile(@Valid @ModelAttribute("profileRequest") ProfileRequest request,
                              BindingResult bindingResult,
                              Model model) {

        if (bindingResult.hasErrors()) {
            // Возвращаем тот же шаблон, где есть форма
            model.addAttribute("profleRequest", request);
            return "profile";
        }
        return customerService.editProfile(request,model);
    }

    @PostMapping("/order")
    public String doOrder(@Valid @ModelAttribute("orderRequest") OrderRequest request,
                          BindingResult bindingResult,
                          Principal principal,
                          HttpSession session,
                          Model model) {
        if (bindingResult.hasErrors()) {
            // Возвращаем тот же шаблон, где есть форма
            double totalSum = cartService.getTotalSum(session);
            double discount = customerService.findByUsername(principal.getName()).getDiscount();
            model.addAttribute("totalSum", totalSum);
            model.addAttribute("discount", discount);
            model.addAttribute("totalSumDisc", getSumWithDiscount(totalSum, discount));
            model.addAttribute("cartItems", cartService.getCartItems(session));
            return "checkout";
        }
        // Получаем данные пользователя и корзины
        CustomerData customer = customerService.getUserInfoByUsername(principal.getName());
        List<CartItemDTO> cartItems = cartService.getCartItems(session);
        double totalSum = cartService.getTotalSum(session);
        double totalQuantity = cartService.getTotalQuantity(session);
        try {
            if (request.isSaveRecvisits() && request.getRecvisitId() > 0) {
                Recvisit recvisit = recvisitRepository.findById(request.getRecvisitId()).orElse(null);
                if (recvisit != null) {
                    recvisit.setCustomerPhone(request.getPhone());
                    recvisit.setCustomerAddress(request.getAddress());
                    recvisit.setComment(request.getComment());

                    recvisitRepository.save(recvisit);
//                recvisitRepository.updateById(request.getRecvisitId(),recvisits);
                }
            }
            // Отправляем письма (пользователю + поставщикам)
            String to = request.getEmail();
            System.out.println("Attempt to send email to:" + to);
            emailService.sendOrderEmails(request, customer, cartItems, session);

            // Очищаем корзину
            session.removeAttribute("cart");

            model.addAttribute("successMessage", "Ваш заказ успешно оформлен!");
            model.addAttribute("cartInfo", new CartInfo());
            return "checkout-success"; // Имя шаблона представления

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "checkout";
        }

    }


}
