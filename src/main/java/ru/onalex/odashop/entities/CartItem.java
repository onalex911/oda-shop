package ru.onalex.odashop.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cart_items")
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "tovar_code", nullable = false)
    private Tovar tovar;

    private int quantity;

    public CartItem(Tovar tovar, int quantity) {
        this.tovar = tovar;
        this.quantity = quantity;
    }
}