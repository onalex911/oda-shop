package ru.onalex.odashop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.onalex.odashop.entities.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerEmail(String email);

    // Поиск по содержимому JSON поля
    @Query(value = "SELECT o FROM Order o WHERE o.orderItems @> :item", nativeQuery = true)
    List<Order> findOrdersContainingItem(@Param("item") String itemJson);
}