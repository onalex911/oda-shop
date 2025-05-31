package ru.onalex.odashop.models;

import lombok.Data;
import ru.onalex.odashop.dtos.CartItemDTO;
import ru.onalex.odashop.entities.CartItem;

import java.util.ArrayList;
import java.util.List;

import static ru.onalex.odashop.utils.ServiceUtils.getSumWithDiscount;

//структура для вывода данных Корзины на любых страницах
@Data
public class CartInfo {
    private int amountPos; //кол-во позиций товара
    private double totalSum; //общая сумма заказа
    private int totalQuantity; //кол-во товара в штуках
    private double totalSumDisc; //общая сумма заказа с учетом скидки
    private double sumPos; // стоимость одной позиции, умн. на кол-во

//    private List<CartItemDTO> cartItems;
    {
        sumPos = 0.0;
    }

    public CartInfo(int amountPos, float totalSum) {
        this.amountPos = amountPos;
        this.totalSum = totalSum;
    }

    public CartInfo(List<CartItemDTO> cart,double discount) {
        if(cart != null) {
//            this.cartItems = cart;
//            this.discount = discount;
            this.amountPos = cart.size();
            this.totalSum = cart.stream()
                    .mapToDouble(item -> item.getTovar().getCena() * item.getQuantity())
                    .sum();
            this.totalQuantity = cart.stream().mapToInt(item -> item.getQuantity()).sum();
            this.totalSumDisc = getSumWithDiscount(this.totalSum,discount);

        }else{
            this.amountPos = 0;
            this.totalSum = 0.0;
            this.totalQuantity = 0;
            this.totalSumDisc = 0.0;
        }
    }

    public CartInfo() {

    }
}
