package ru.onalex.odashop.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Структура для типов поиска ключ – русское наим для фронта
 */
@Data
@AllArgsConstructor
public class SortField {
    String field;
    String caption;
}
