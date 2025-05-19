package ru.onalex.odashop.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.onalex.odashop.entities.Customer;
import ru.onalex.odashop.entities.Recvisit;
import ru.onalex.odashop.entities.Role;
import ru.onalex.odashop.models.UserInfo;
import ru.onalex.odashop.repositories.CustomerRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//получение инф. о пользователе по его имени, с которым он авторизовался
@Service
public class CustomerService implements UserDetailsService {
    private  CustomerRepository customerRepository;

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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


}
