package ru.onalex.odashop.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.onalex.odashop.entities.GrupTov;
import ru.onalex.odashop.entities.Tovar;

import java.util.List;

//@Repository
@RepositoryRestResource(path = "tovar")
public interface TovarRepository extends JpaRepository<Tovar, Integer> {
    List<Tovar> findByGrupTov(int grupTov);

//    @Query(value="SELECT t.* FROM tovar t RIGHT JOIN bj_groups bg " +
//            "ON bg.group_id = t.gruptov " +
//            "WHERE bg.group_name_lat LIKE :alias AND t.ostatok > 0",nativeQuery = true)
    @Query(value="SELECT * FROM tovar t WHERE t.ostatok > 0 AND purl LIKE CONCAT('%/',:alias,'/%') ORDER BY t.nomer",nativeQuery = true)
//    List<Tovar> findTovarByAlias(String alias);
    Page<Tovar> findTovarByAlias(String alias, Pageable pageable);

    @Query(value="SELECT * FROM tovar t WHERE t.code=:code AND t.ostatok > 0 ORDER BY t.nomer",nativeQuery = true)
    Tovar findExistTovarByCode(int code);

    @Query(value="SELECT * FROM tovar t WHERE t.code=:id",nativeQuery = true)
    Tovar findTovarById(int id);

    @Query(value="SELECT * FROM tovar t WHERE t.gruptov = :groupId ORDER BY nomer",nativeQuery = true)
    List<Tovar> findTovarByGroupId(int groupId);

//    @Query(value="SELECT t.code, t.blok, t.pic, t.dop, t.code1, t.name, t.nomer, t.cena, t.ostatok " +
//            "FROM tovar t WHERE t.gruptov = :groupId ORDER BY nomer",nativeQuery = true)
//    List<Tovar> findTovarByGroupIdShort(int groupId);

    @Modifying
    @Transactional
    @Query(value="UPDATE tovar SET blok=:status WHERE code=:id", nativeQuery=true)
    void setBlokStatus(int id, int status) ;

    List<Tovar> findByTovNameContainingIgnoreCaseOrderByTovName(String name);
    List<Tovar> findByTovNameStartsWithOrderByTovName(String name);

    List<Tovar> findByDopContainingOrderByDop(String productCode);
    List<Tovar> findByDopStartsWithOrderByDop(String productCode);

    List<Tovar> findByCenaEqualsOrderByCena(Double price);
    List<Tovar> findByCenaBetweenOrderByCena(Double minPrice, Double maxPrice);
    List<Tovar> findByCenaGreaterThanOrderByCena(Double minPrice);
    List<Tovar> findByCenaLessThanOrderByCena(Double maxPrice);
}
