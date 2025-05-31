package ru.onalex.odashop.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.onalex.odashop.entities.GrupTov;

import java.util.List;

@Repository
public interface GrupTovRepository extends JpaRepository<GrupTov, Integer> {
    List<GrupTov> findAll();

    //Legacy: в "общей" БД бижутерия хранится в поле rod под номером 1413
    @Query(value="SELECT g.* FROM gruptov g WHERE g.rod='1413' ORDER BY g.nomer",nativeQuery = true)
    List<GrupTov> findBijou();
    //для фронта - выводим только активные
    @Query(value="SELECT g.* FROM gruptov g WHERE g.blok=0 AND g.rod='1413' ORDER BY g.nomer",nativeQuery = true)
    List<GrupTov> findBijouActive();

    //Legacy: алиас хранится в последнем сегменте пути в поле purl
    @Query(value="SELECT g.* FROM gruptov g WHERE g.blok=0 AND g.purl LIKE CONCAT('%/',:alias)",nativeQuery = true)
    GrupTov findByAlias(String alias);

    @Query(value="SELECT g.* FROM gruptov g WHERE g.code=:id",nativeQuery = true)
    GrupTov findById(int id);

    @Modifying
    @Transactional
    @Query(value="UPDATE gruptov SET blok=:status WHERE code=:id", nativeQuery=true)
    void setBlokStatus(int id, int status) ;
}
