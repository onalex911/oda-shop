package ru.onalex.odashop.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="bj_groups")
@Data
public class BjGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="group_name_ru",columnDefinition = "varchar(50)")
    @NotNull()
    @NotBlank(message = "Имя группы не может быть пустым!")
    private String groupNameRu;

    @Column(name="group_name_lat",columnDefinition = "varchar(50)")
    @NotNull()
    @NotBlank(message = "Имя группы не может быть пустым!")
    private String groupNameLat;

    @Column(name="group_id",columnDefinition = "integer")
    @NotNull()
    @NotBlank(message = "Внутренний номер группы должен присутствовать!")
    @NaturalId
    private Integer groupId; //legacy - значение id группы, используется в ссылках и назв. изображений групп

    @Column(name="group_order",columnDefinition = "integer")
    private Integer order;

    @Column(name="active")
    @NotNull
    private Boolean isActive;

    @OneToMany(mappedBy = "grupTov", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tovar> grupTovSet = new HashSet<>();
}
