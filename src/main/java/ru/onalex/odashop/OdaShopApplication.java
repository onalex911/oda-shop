package ru.onalex.odashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = {"ru.onalex", "ru.onalex.controllers", "ru.onalex.repositories", "ru.onalex.entities"})
public class OdaShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(OdaShopApplication.class, args);
    }

}
