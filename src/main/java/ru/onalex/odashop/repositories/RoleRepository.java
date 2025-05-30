package ru.onalex.odashop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.onalex.odashop.entities.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
//    @Query(value = "SELECT r.* FROM roles r WHERE r.name LIKE :user", nativeQuery = true)
    Optional<Role> findByName(String name);
}
