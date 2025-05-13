package ru.onalex.odashop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.onalex.odashop.entities.GrupTov;

import java.util.List;

@Repository
public interface GrupTovRepository extends JpaRepository<GrupTov, Integer> {
    List<GrupTov> findAll();

    //Legacy: в "общей" БД бижутерия хранится в поле rod под номером 1413
    @Query(value="SELECT * FROM gruptov WHERE blok=0 AND rod='1413'",nativeQuery = true)
    List<GrupTov> findBijou();

    //Legacy: алиас хранится в последнем сегменте пути в поле purl
    @Query(value="SELECT * FROM gruptov WHERE blok=0 AND purl LIKE CONCAT('%/',:alias)",nativeQuery = true)
    GrupTov findByAlias(String alias);



}
