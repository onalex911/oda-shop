package ru.onalex.odashop.entities;


import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private String orderNumber;
    private LocalDateTime orderDate;
    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private String customerPhone;
    private String status;

    //@Type(JsonType.class)
    //@Column(columnDefinition = "jsonb")
    //private List<CartItem> orderItems;

    private BigDecimal totalAmount;

    // Геттеры и сеттеры
}