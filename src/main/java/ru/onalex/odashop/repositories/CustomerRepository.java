package ru.onalex.odashop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.onalex.odashop.dtos.ProfileDTO;
import ru.onalex.odashop.entities.Customer;
import ru.onalex.odashop.entities.Recvisit;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByUsername(String username);

    @Query(value="SELECT r.* from customers c " +
            "left join rekvizitu_schet r on c.id = r.customer WHERE c.username=:username", nativeQuery=true)
    List<Recvisit> getRecvisitsByUsername(@Param("username") String username);

    @Query(value = """
        SELECT 
            c.id as customerId,
            c.username as username,
            c.contact_name as contactName,
            c.discount as discount,
            r.poluchatel as orgName,
            r.inn as inn,
            r.postavshik_adres as address,
            r.postavshik_telefon as phone,
            r.comment as comment
        FROM customers c
        LEFT JOIN rekvizitu_schet r ON c.id = r.customer 
        WHERE c.username = :username""",
            nativeQuery = true)
    List<ProfileDTO> getProfileDataByUsername(@Param("username") String username);

//    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.recvisitSet WHERE c.username = :username")
//    Optional<Customer> findByIdWithRecvisits(@Param("username") String username);

    boolean existsByUsername(String username);


}
