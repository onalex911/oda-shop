package ru.onalex.odashop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.onalex.odashop.entities.Customer;
import ru.onalex.odashop.entities.Recvisit;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByUsername(String username);

    @Query(value="SELECT r.* from customers c " +
            "left join rekvizitu_schet r on c.id = r.customer WHERE c.username=:username", nativeQuery=true)
    List<Recvisit> getRecvisitsByUsername(String username);

    boolean existsByUsername(String username);


}
