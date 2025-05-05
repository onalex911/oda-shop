package ru.onalex.odashop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.onalex.odashop.entities.BjGroup;

import java.util.List;

@Repository
public interface BJGroupRepository extends JpaRepository<BjGroup,Integer> {
    @Query(value="SELECT * FROM bj_groups WHERE active=true",nativeQuery = true)
    List<BjGroup> findAllActive();

    BjGroup findByGroupId(int groupId);

    @Query(value="SELECT * FROM bj_groups WHERE group_name_lat LIKE :alias",nativeQuery = true)
    BjGroup findByAlias(String alias);
}
