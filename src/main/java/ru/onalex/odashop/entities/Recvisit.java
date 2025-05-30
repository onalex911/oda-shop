package ru.onalex.odashop.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="rekvizitu_schet")
@Data
public class Recvisit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//  `id` int(10) NOT NULL AUTO_INCREMENT,
    private long id;
//  `vid` int(1) DEFAULT NULL,
    private int vid;
//  `nds` int(1) DEFAULT NULL,
    private int nds;
//  `poluchatel` varchar(250) COLLATE cp1251_bin DEFAULT NULL,
    @Column(name="poluchatel", columnDefinition = "varchar(250)")
    private String orgName;
//  `inn` varchar(20) COLLATE cp1251_bin DEFAULT NULL,
    @Column(columnDefinition = "varchar(20)")
    private String inn;
//  `kpp` varchar(20) COLLATE cp1251_bin DEFAULT NULL,
    @Column(columnDefinition = "varchar(20)")
    private String kpp;
//  `bank` varchar(250) COLLATE cp1251_bin DEFAULT NULL,
    @Column(columnDefinition = "varchar(250)")
    private String bank;
//  `bik` varchar(20) COLLATE cp1251_bin DEFAULT NULL,
    @Column(columnDefinition = "varchar(20)")
    private String bik;
//  `kor_schet` varchar(250) COLLATE cp1251_bin DEFAULT NULL,
    @Column(columnDefinition = "varchar(250)")
    private String kor_schet;
//  `raschetnuy_schet` varchar(250) COLLATE cp1251_bin DEFAULT NULL,
    @Column(columnDefinition = "varchar(250)")
    private String raschetnuy_schet;
//  `postavshik` varchar(250) COLLATE cp1251_bin DEFAULT NULL,
    @Column(name="postavshik", columnDefinition = "varchar(250)")
    private String orgFullName;
//  `postavshik_adres` varchar(250) COLLATE cp1251_bin DEFAULT NULL,
    @Column(name="postavshik_adres",columnDefinition = "varchar(250)")
    private String customerAddress;
//  `postavshik_telefon` varchar(250) COLLATE cp1251_bin DEFAULT NULL,
    @Column(name="postavshik_telefon",columnDefinition = "varchar(250)")
    private String customerPhone;
//  `gruzootpravitel` varchar(250) COLLATE cp1251_bin DEFAULT NULL,
    @Column(columnDefinition = "varchar(250)")
    private String gruzootpravitel;
//  `gruzootpravitel_adres` varchar(250) COLLATE cp1251_bin DEFAULT NULL,
    @Column(columnDefinition = "varchar(250)")
    private String gruzootpravitel_adres;
//  `gruzootpravitel_telefon` varchar(250) COLLATE cp1251_bin DEFAULT NULL,
    @Column(columnDefinition = "varchar(250)")
    private String gruzootpravitel_telefon;
//  `pechat` varchar(250) COLLATE cp1251_bin DEFAULT NULL,
    @Column(columnDefinition = "varchar(250)")
    private String pechat;
//  `rukovoditel` varchar(250) COLLATE cp1251_bin DEFAULT NULL,
    @Column(columnDefinition = "varchar(250)")
    private String rukovoditel;
//  `buhgalter` varchar(250) COLLATE cp1251_bin DEFAULT NULL,
    @Column(columnDefinition = "varchar(250)")
    private String buhgalter;
//  `comment` text COLLATE cp1251_bin,
    @Column(columnDefinition = "text")
    private String comment;

    //у нескольких реквизитов - один клиент
    @ManyToOne
    @JoinColumn(name = "customer")
    private Customer customer;



}
