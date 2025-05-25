package ru.onalex.odashop.models;

import lombok.Data;
import ru.onalex.odashop.dtos.CartItemDTO;
import ru.onalex.odashop.entities.CartItem;

import java.util.List;

//структура для вывода данных Корзины на любых страницах
@Data
public class CartInfo {
    private int amountPos;
    private double totalSum;

    public CartInfo(int amountPos, float totalSum) {
        this.amountPos = amountPos;
        this.totalSum = totalSum;
    }

    public CartInfo(List<CartItemDTO> cart) {
        if(cart != null) {
            this.amountPos = cart.size();
            this.totalSum = cart.stream()
                    .mapToDouble(item -> item.getTovar().getCena() * item.getQuantity())
                    .sum();

        }else{
            this.amountPos = 0;
            this.totalSum = 0;
        }
    }
}
