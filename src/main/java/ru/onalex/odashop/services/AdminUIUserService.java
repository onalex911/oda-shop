package ru.onalex.odashop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.onalex.odashop.entities.AdminUIUser;
import ru.onalex.odashop.repositories.AdminUIUserRepository;

import java.util.List;

@Service
public class AdminUIUserService {
    AdminUIUserRepository adminUIUserRepository;

    @Autowired
    private void setAdminUIUserRepository(AdminUIUserRepository adminUIUserRepository){
        this.adminUIUserRepository = adminUIUserRepository;
    }

    public long count(){
        return adminUIUserRepository.count();
    }

    public List<AdminUIUser> findAll() {
        return adminUIUserRepository.findAll();
    }
    public AdminUIUser findById(long id) {
        return adminUIUserRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Нет администратора с указанным id ("+ id + ")!"));
    }

    public void save(AdminUIUser adminUIUser) {
        adminUIUserRepository.save(adminUIUser);
    }
}
