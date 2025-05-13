package ru.onalex.odashop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.onalex.odashop.entities.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteByCartIdAndTovarId(Long cartId, Long tovarId);
}