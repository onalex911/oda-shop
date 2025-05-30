package ru.onalex.odashop.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.onalex.odashop.configs.SecurityConfig;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(name="username")
    private String username;

    @NotBlank
    @Column(name="contact_name")
    private String contactName;

    @NotBlank
    private String password;
    private double discount;

//   у клиента может быть несколько реквизитов
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY) // Или EAGER
    private Set<Recvisit> recvisitSet = new HashSet<>();

    //у клиента (пользователя) м.б. много ролей; роль, в свою очередь м.б. назначена множеству пользователей
//    @ManyToMany(fetch = FetchType.EAGER)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "customers_roles",
    joinColumns = @JoinColumn(name="customer_id"),
    inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();

}
