package ru.onalex.odashop.services;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import ru.onalex.odashop.dtos.RecvisitDTO;
import ru.onalex.odashop.entities.Customer;
import ru.onalex.odashop.entities.Recvisit;
import ru.onalex.odashop.entities.Role;
import ru.onalex.odashop.models.ProfileRequest;
import ru.onalex.odashop.models.RegisterRequest;
import ru.onalex.odashop.models.CustomerData;
import ru.onalex.odashop.repositories.CustomerRepository;
import ru.onalex.odashop.repositories.RecvisitRepository;
import ru.onalex.odashop.repositories.RoleRepository;

import java.security.Principal;
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
    private final RecvisitRepository recvisitRepository;


    //обертка для получения пользователя (чтобы не обращаться напрямую в репозиторий)
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Не найден клиент с именем: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Customer customer = customerRepository.findByUsername(username);
        Customer customer = customerRepository.findByUsernameWithRecvisits(username);
        if (customer == null) {
            throw new UsernameNotFoundException(String.format("Пользователь с логином %s не найден!", username));
        }
        return new org.springframework.security.core.userdetails.User(
                customer.getUsername(), customer.getPassword(), mapRolesToAuthorities(customer.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    public CustomerData getUserInfoByUsername(String username) {
        Customer customer = findByUsername(username);
        List<Recvisit> recvisits = customerRepository.getRecvisitsByUsername(username);
        return new CustomerData(customer, recvisits);

    }

    public void doRegistration(RegisterRequest request, RecvisitDTO recvisitDTO) {
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
        try {
            Role userRole = roleRepository.findByName("ROLE_" + DEFAULT_ROLE)
                    .orElseThrow(() -> new RuntimeException("Роль USER не найдена"));

            // Создаем коллекцию, если она null
            if (customer.getRoles() == null) {
                customer.setRoles(new HashSet<>());
            }

            // Добавляем роль
            customer.getRoles().add(userRole);

            // Сохраняем клиента (если нужно)
            customerRepository.save(customer);
            System.out.println("Customer: " + customer.getUsername() + " saved to DB");
            Recvisit recvisit = new Recvisit();
            recvisit.setCustomer(customer);
            recvisitRepository.save(recvisit);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении нового пользователя: " + e.getMessage());
        }

    }

    @Transactional(readOnly = true)
    public ProfileRequest getProfileRequest(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }

//        Customer customer = customerRepository.findByUsernameWithRecvisits(name);
        Customer customer = findByUsername(name);
        if (customer == null) {
            throw new EntityNotFoundException("Клиент с именем " + name + " не найден");
        }

        List<Recvisit> recvisits = customerRepository.getRecvisitsByUsername(name);
        if(!recvisits.isEmpty()) {
            Recvisit recvisit = recvisits.get(0);
            return ProfileRequest.builder()
                    .username(customer.getUsername())
                    .contactName(customer.getContactName())
                    .discount(customer.getDiscount())
                    .customerId(customer.getId())
                    .recvisitId(recvisit.getId())
                    .address(recvisit.getCustomerAddress())
                    .phone(recvisit.getCustomerPhone())
                    .comment(recvisit.getComment())
                    .build();
        }else{
            return ProfileRequest.builder()
                    .username(customer.getUsername())
                    .contactName(customer.getContactName())
                    .discount(customer.getDiscount())
                    .customerId(customer.getId())
                    .build();
        }
    }

    public String editProfile(@Valid ProfileRequest request, Model model) {
        try {
            // Получаем данные пользователя
            Customer customer = findByUsername(request.getUsername());
            customer.setContactName(request.getContactName());
            customerRepository.save(customer);

            Recvisit recvisit = recvisitRepository.findById(request.getRecvisitId()).orElse(null);
            if (recvisit != null) {
                recvisit.setCustomerPhone(request.getPhone());
                recvisit.setCustomerAddress(request.getAddress());
                recvisit.setComment(request.getComment());

                recvisitRepository.save(recvisit);
                model.addAttribute("success", "Ваши реквизиты успешно обновлены!");
            }else{
                model.addAttribute("errorMessage", "Не найдены еквизиты успешно обновлены!");

            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "profile";
    }

}
