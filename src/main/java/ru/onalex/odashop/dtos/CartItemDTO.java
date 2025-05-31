package ru.onalex.odashop.dtos;

import jakarta.persistence.*;
import lombok.Data;
import ru.onalex.odashop.entities.Cart;
import ru.onalex.odashop.entities.Tovar;

@Data
public class CartItemDTO {
    private Long id;
    private TovarDTO tovar;
    private int quantity;
    private double sumPos; //стоимость заказанной позиции

    public CartItemDTO(TovarDTO tovar, int quantity) {
        this.tovar = tovar;
        this.quantity = quantity;
        this.sumPos = Math.ceil(tovar.getCena() * quantity * 100) / 100;
    }
}
