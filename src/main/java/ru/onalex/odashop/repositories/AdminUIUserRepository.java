package ru.onalex.odashop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.onalex.odashop.entities.AdminUIUser;

@RepositoryRestResource(path = "admin-users")
public interface AdminUIUserRepository extends JpaRepository<AdminUIUser, Long> {
}
