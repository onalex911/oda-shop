package ru.onalex.odashop.dtos;

import jakarta.persistence.*;
import lombok.Data;
import ru.onalex.odashop.entities.Customer;

@Data
public class RecvisitDTO {
    private long id;
    private String customerName;
    private String custometFullName;
    private String customerAddress;
    private String customerPhone;
    private String comment;

}
