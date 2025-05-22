package ru.onalex.odashop.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SortField {
    String field;
    String caption;
}
