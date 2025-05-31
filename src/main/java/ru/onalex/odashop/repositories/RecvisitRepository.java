package ru.onalex.odashop.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.onalex.odashop.dtos.ProfileDTO;
import ru.onalex.odashop.dtos.RecvisitDTO;
import ru.onalex.odashop.entities.Customer;
import ru.onalex.odashop.entities.Recvisit;

import java.util.List;

@Repository
public interface RecvisitRepository extends JpaRepository<Recvisit, Long> {
    Recvisit findByCustomer(Customer customer);

//    @Query(value="UPDATE rekvizitu_schet SET   WHERE customer=:regData.id", nativeQuery=true)
//    void saveRegData(@Param("regData") RecvisitDTO regData);

}
