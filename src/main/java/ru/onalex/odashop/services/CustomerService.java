package ru.onalex.odashop.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.onalex.odashop.entities.Customer;
import ru.onalex.odashop.entities.Recvisit;
import ru.onalex.odashop.entities.Role;
import ru.onalex.odashop.models.ProfileRequest;
import ru.onalex.odashop.models.RegisterRequest;
import ru.onalex.odashop.models.CustomerData;
import ru.onalex.odashop.repositories.CustomerRepository;
import ru.onalex.odashop.repositories.RoleRepository;

import java.util.*;
import java.util.stream.Collectors;

//получение инф. о пользователе по его имени, с которым он авторизовался
@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {

    private static final String DEFAULT_ROLE = "USER";

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
//    private final EmailService emailService;
//    private final CartService cartService;

//    public CustomerService(
//            CustomerRepository customerRepository,
//            PasswordEncoder passwordEncoder,
//            RoleRepository roleRepository,
//            EmailService emailService,
//            CartService cartService) {
//        this.customerRepository = customerRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.roleRepository = roleRepository;
//        this.emailService = emailService;
//        this.cartService = cartService;
//    }

    //обертка для получения пользователя (чтобы не обращаться напрямую в репозиторий)
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Customer customer = customerRepository.findByUsername(username);
        if(customer == null) {
            throw new UsernameNotFoundException(String.format("Пользователь с логином %s не найден!", username));
        }
        return new org.springframework.security.core.userdetails.User(
                customer.getUsername(), customer.getPassword(), mapRolesToAuthorities(customer.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public CustomerData getUserInfoByUsername(String username){
        Customer customer = findByUsername(username);
        List<Recvisit> recvisits = customerRepository.getRecvisitsByUsername(username);
        return new CustomerData(customer,recvisits);

    }

    public void doRegistration(RegisterRequest request) {
        if (customerRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Пользователь с таким логином уже существует");
        }
        if (!request.getPassword().equals(request.getPassword2())) {
            throw new RuntimeException("Пароли должны совпадать!");
        }

        Customer customer = new Customer();
        customer.setUsername(request.getUsername());
        customer.setContactName(request.getContactName());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setDiscount(0.0);

        Role userRole = roleRepository.findByName(DEFAULT_ROLE);
//                .orElseThrow(() -> new RuntimeException("Роль USER не найдена"));
        customer.getRoles().add(userRole);

        customerRepository.save(customer);

    }

    @Transactional
    public void testRecvisits(String username) {
        Customer customer = customerRepository.findByUsername(username);
        if (customer == null) {
            throw new RuntimeException("Customer not found");
        }

        // Создаем копию коллекции для безопасной работы
        Set<Recvisit> recvisits = new HashSet<>(customer.getRecvisitSet());
        System.out.println("Recvisits size: " + recvisits.size());

        // Пример безопасного перебора
        synchronized (customer.getRecvisitSet()) {
            for (Recvisit r : recvisits) {
                System.out.println(r.getCustomerName());
            }
        }
    }

    @Transactional
    public ProfileRequest getProfileRequest(String name) {
        // Получаем клиента с реквизитами
        Customer customer = customerRepository.findByIdWithRecvisits(name)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Создаем DTO
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setUsername(customer.getUsername());
        profileRequest.setContactName(customer.getContactName());

        // Обрабатываем реквизиты (может быть null)
        Optional<Recvisit> firstRecvisit = customer.getRecvisitSet().stream().findFirst();

        if (firstRecvisit.isPresent()) {
            Recvisit recvisits = firstRecvisit.get();
            profileRequest.setOrganization(recvisits.getCustomerName());
            profileRequest.setAddress(recvisits.getCustomerAddress());
            profileRequest.setPhone(recvisits.getCustomerPhone());
            profileRequest.setComment(recvisits.getComment());
        } else {
            // Устанавливаем значения по умолчанию, если реквизитов нет
            profileRequest.setOrganization("");
            profileRequest.setAddress("");
            profileRequest.setPhone("");
            profileRequest.setComment("");
        }
        return profileRequest;
    }

//    public void doOrder(@Valid OrderRequest request,
//                        Customer customer,
//                        HttpSession session,
//                        Model model) {
//        List<CartItemDTO> cartItems = cartService.getCartItems(session);
//        double total = cartService.getTotal(session);
//
//        // Отправляем email
//        emailService.sendOrderEmail(
//                customer.getUsername(),
//                "Ваш заказ в магазине",
//                cartItems,
//                total
//        );
//
//        // Очищаем корзину после отправки
//        session.removeAttribute("cart");
//        System.out.println("order request: " + request);
//    }

}
