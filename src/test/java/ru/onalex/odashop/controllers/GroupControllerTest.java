package ru.onalex.odashop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.onalex.odashop.entities.GrupTov;
import ru.onalex.odashop.repositories.GrupTovRepository;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GroupControllerTest {

    @Autowired
    private GrupTovRepository groupRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    public void create_group_valid_data_return_group() {
        GrupTov group = new GrupTov();
        AtomicReference<String> locationHeader = new AtomicReference<>("");

        group.setGrupName("Коллекция «Супер»");
        group.setRod("1413");
        group.setPicPreview("/static/images/super.jpg");
        group.setNomer(1);
        group.setPurl("/catalog/bizhuteriya/kollekciya_super");
        group.setBlok(0);
        group.setKeywords("бижутерия коллекция супер купить экстравагантный");
        group.setDescription("бижутерия оригинального дизайна, авторская работа");
        mockMvc.perform(
                        post("/adminpanel/categories/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(group)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(result -> {
                    // Получаем URI из заголовка Location
                    locationHeader.set(result.getResponse().getHeader("Location"));
                });
//        System.out.println("Returned URI is: " + locationHeader.get());
        mockMvc.perform(
                        get(locationHeader.get()))
                .andExpect(status().isOk());
    }

    //4 Тестирование валидации данных при создании ресурса
    @Test
    public void create_group_invalid_data_return_bad_fields() throws Exception {
        GrupTov group = new GrupTov();
        AtomicReference<String> locationHeader = new AtomicReference<>("");
        String dateString = "2025-03-18";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        group.setGrupName("");
        group.setRod("1413");
        group.setPicPreview("/static/images/super.jpg");
        group.setNomer(1);
        group.setPurl("/catalog/bizhuteriya/kollekciya_super");
        group.setOst(0);
        mockMvc.perform(
                        post("/adminpanel/categories/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(group)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.grupname").value("Имя группы не должно быть пустым!"))
                .andExpect(jsonPath("$.ost").value("Остаток не должен быть меньше 1!"));
    }

    //5 Тестирование PUT запроса для обновления ресурса
//    @Test
//    public void update_group_valid_data_check_updated_group() throws Exception {
//        int idForUpdate = 40;
//        double newSalary = 9999.99;
//        double newPremium = 1111.11;
//        String newPosition = "Higher Decision";
//        GrupTov group = groupRepository.findById(idForUpdate).get();
//        GrupTov updatedGrupTov = new GrupTov();
//        updatedGrupTov.setSalary(newSalary);
//        updatedGrupTov.setPremium(newPremium);
//        updatedGrupTov.setPosition(newPosition);
//        mockMvc.perform(
//                        put("/api/group/{id}",idForUpdate)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(updatedGrupTov)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.position",is(newPosition)))
//                .andExpect(jsonPath("$.salary",is(newSalary)))
//                .andExpect(jsonPath("$.premium",is(newPremium)))
//                //проверяем целостность неизмененных данных
//                .andExpect(jsonPath("$.name",is(group.getName())))
//                .andExpect(jsonPath("$.surname",is(group.getSurname())))
//                .andExpect(jsonPath("$.isAssistant",is(group.getIsAssistant())))
//                .andExpect(jsonPath("$.isProfessor",is(group.getIsProfessor())));
//
//        mockMvc.perform(
//                        put("/api/group/{id}",55)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(updatedGrupTov)))
//                .andExpect(status().isNotFound());
//    }
//

}
