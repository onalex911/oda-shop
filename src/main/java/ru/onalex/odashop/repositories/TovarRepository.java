package ru.onalex.odashop.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.onalex.odashop.entities.Tovar;

import java.util.List;
import java.util.Optional;

//@Repository
@RepositoryRestResource(path = "tovar")
public interface TovarRepository extends JpaRepository<Tovar, Integer>, JpaSpecificationExecutor<Tovar> {
    List<Tovar> findByGrupTov(int grupTov);

//    @Query(value="SELECT t.* FROM tovar t RIGHT JOIN bj_groups bg " +
//            "ON bg.group_id = t.gruptov " +
//            "WHERE bg.group_name_lat LIKE :alias AND t.ostatok > 0",nativeQuery = true)
    @Query(value="SELECT * FROM tovar t WHERE t.blok = 0 AND t.ostatok > 0 AND t.purl LIKE CONCAT('%/',:alias,'/%')",nativeQuery = true)
    Page<Tovar> findTovarByAlias(String alias, Pageable pageable);

    @Query(value="SELECT * FROM tovar t WHERE t.code=:code AND t.ostatok > 0 ORDER BY t.nomer",nativeQuery = true)
    Tovar findExistTovarByCode(int code);

//    @Query(value="SELECT * FROM tovar t WHERE t.code=:id",nativeQuery = true)
    Optional<Tovar> findTovarById(int id);

    @Query(value="SELECT * FROM tovar t WHERE t.gruptov = :groupId ORDER BY t.nomer",nativeQuery = true)
    List<Tovar> findTovarByGroupId(int groupId);

//    @Query(value="SELECT t.code, t.blok, t.pic, t.dop, t.code1, t.name, t.nomer, t.cena, t.ostatok " +
//            "FROM tovar t WHERE t.gruptov = :groupId ORDER BY nomer",nativeQuery = true)
//    List<Tovar> findTovarByGroupIdShort(int groupId);

    @Modifying
    @Transactional
    @Query(value="UPDATE tovar SET blok=:status WHERE code=:id", nativeQuery=true)
    void setBlokStatus(@Param("id") int id, @Param("status") int status) ;

    @Query(value="SELECT * FROM tovar t WHERE UPPER(t.name) LIKE CONCAT('%',:name,'%') " +
            "AND t.gruptov>=1418 AND t.gruptov<=1430 AND t.cena > 0 AND t.ostatok > 0 AND t.blok = 0 ORDER BY t.name",nativeQuery = true)
    Page<Tovar> findByTovNameContainingIgnoreCaseOrderByTovNamePub(String name, Pageable pageable);
    List<Tovar> findByTovNameContainingIgnoreCaseOrderByTovName(String name);

    @Query(value="SELECT * FROM tovar t WHERE UPPER(t.name) LIKE CONCAT(:name,'%') " +
            "AND t.gruptov>=1418 AND t.gruptov<=1430 AND t.cena > 0 AND t.ostatok > 0 AND t.blok = 0 ORDER BY t.name",nativeQuery = true)
    Page<Tovar> findByTovNameStartsWithIgnoreCaseOrderByTovNamePub(String name, Pageable pageable);
    List<Tovar> findByTovNameStartsWithIgnoreCaseOrderByTovName(String name);
    @Query(value="SELECT * FROM tovar t WHERE UPPER(t.dop) LIKE CONCAT('%',:productCode,'%') " +
            "AND t.gruptov>=1418 AND t.gruptov<=1430 AND t.cena > 0 AND t.ostatok > 0 AND t.blok = 0 ORDER BY t.dop",nativeQuery = true)
    Page<Tovar> findByDopContainingIgnoreCaseOrderByDopPub(String productCode, Pageable pageable);
    List<Tovar> findByDopContainingIgnoreCaseOrderByDop(String productCode);
    @Query(value="SELECT * FROM tovar t WHERE UPPER(t.dop) LIKE CONCAT(:productCode,'%') " +
            "AND t.gruptov>=1418 AND t.gruptov<=1430 AND t.cena > 0 AND t.ostatok > 0 AND t.blok = 0 ORDER BY t.dop",nativeQuery = true)
    Page<Tovar> findByDopStartsWithIgnoreCaseOrderByDopPub(String productCode, Pageable pageable);
    List<Tovar> findByDopStartsWithIgnoreCaseOrderByDop(String productCode);

    @Query(value="SELECT * FROM tovar t WHERE t.cena = :price " +
            "AND t.gruptov>=1418 AND t.gruptov<=1430 AND t.cena > 0 AND t.ostatok > 0 AND t.blok = 0 ORDER BY t.cena",nativeQuery = true)
    Page<Tovar> findByCenaEqualsOrderByCenaPub(Double price, Pageable pageable);
    List<Tovar> findByCenaEqualsOrderByCena(Double price);
    @Query(value="SELECT * FROM tovar t WHERE t.cena BETWEEN :minPrice AND :maxPrice " +
            "AND t.gruptov>=1418 AND t.gruptov<=1430 AND t.ostatok > 0 AND t.blok = 0 ORDER BY t.cena",nativeQuery = true)
    Page<Tovar> findByCenaBetweenOrderByCenaPub(Double minPrice, Double maxPrice, Pageable pageable);
    List<Tovar> findByCenaBetweenOrderByCena(Double minPrice, Double maxPrice);

    @Query(value="SELECT * FROM tovar t WHERE t.cena > :minPrice " +
            "AND t.gruptov>=1418 AND t.gruptov<=1430 AND t.blok = 0 AND t.ostatok > 0 ORDER BY t.cena",nativeQuery = true)
    Page<Tovar> findByCenaGreaterThanOrderByCenaPub(Double minPrice, Pageable pageable);
    List<Tovar> findByCenaGreaterThanOrderByCena(Double minPrice);

    @Query(value="SELECT * FROM tovar t WHERE t.cena BETWEEN 0.01 AND :maxPrice " +
            "AND t.gruptov>=1418 AND t.gruptov<=1430 AND t.blok = 0 AND t.ostatok > 0 ORDER BY t.cena",nativeQuery = true)
    Page<Tovar> findByCenaLessThanOrderByCenaPub(Double maxPrice, Pageable pageable);
    List<Tovar> findByCenaLessThanOrderByCena(Double maxPrice);

//    int countTovarByAlias(String alias);
}
