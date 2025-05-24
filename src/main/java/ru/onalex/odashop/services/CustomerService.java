package ru.onalex.odashop.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.onalex.odashop.entities.Customer;
import ru.onalex.odashop.entities.Recvisit;
import ru.onalex.odashop.entities.Role;
import ru.onalex.odashop.models.RegisterRequest;
import ru.onalex.odashop.models.UserInfo;
import ru.onalex.odashop.repositories.CustomerRepository;
import ru.onalex.odashop.repositories.RoleRepository;

import java.util.*;
import java.util.stream.Collectors;

//получение инф. о пользователе по его имени, с которым он авторизовался
@Service
public class CustomerService implements UserDetailsService {

    private static final String DEFAULT_ROLE = "USER";

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public CustomerService(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

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

    public UserInfo getUserInfoByUsername(String username){
        Customer customer = findByUsername(username);
        List<Recvisit> recvisits = customerRepository.getRecvisitsByUsername(username);
        return new UserInfo(customer,recvisits);

    }

    public String doRegistration(RegisterRequest request, Model model) {
        String errorMessage = "";
        if (findByUsername(request.getUsername()) != null) {
            errorMessage = "Пользователь с именем "+request.getUsername()+" уже зарегистрирован!";
        }else{
            try {
                Customer customer = new Customer();
                customer.setUsername(request.getUsername());
                customer.setContactName(request.getContactName());
                customer.setPassword(passwordEncoder.encode(request.getPassword())); // Шифруем пароль
                customer.setDiscount(0.0); // Скидка по умолчанию

                Role userRole = roleRepository.findByName(DEFAULT_ROLE);
                customer.getRoles().add(userRole);
                customerRepository.save(customer);
            }catch (Exception e) {
                errorMessage = e.getMessage();
            }
        }
        if(errorMessage.isEmpty()) errorMessage ="it's ok!";
//        if(!errorMessage.isEmpty()){
            model.addAttribute("errorMessage", errorMessage);
//        }

        return "account";

    }
}
