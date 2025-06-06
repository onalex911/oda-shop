package ru.onalex.odashop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.onalex.odashop.entities.Customer;
import ru.onalex.odashop.entities.Recvisit;

import java.util.List;

//структура для вывода данных пользователя с учетом реквизитов
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerData {
    private Customer customer;
    private List<Recvisit> recvisits;

}
