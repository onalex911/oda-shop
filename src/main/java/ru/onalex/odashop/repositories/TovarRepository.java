package ru.onalex.odashop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.onalex.odashop.entities.GrupTov;
import ru.onalex.odashop.entities.Tovar;

import java.util.List;

@Repository
public interface TovarRepository extends JpaRepository<Tovar, Integer> {
    List<Tovar> findByGrupTov(int grupTov);

    @Query(value="SELECT t.* FROM tovar t RIGHT JOIN bj_groups bg " +
            "ON bg.group_id = t.gruptov " +
            "WHERE bg.group_name_lat LIKE :alias AND t.ostatok > 0",nativeQuery = true)
    List<Tovar> findTovarByAlias(String alias);

    @Query(value="SELECT * FROM tovar t WHERE code=:code AND t.ostatok > 0",nativeQuery = true)
    Tovar findExistTovarByCode(int code);

}
