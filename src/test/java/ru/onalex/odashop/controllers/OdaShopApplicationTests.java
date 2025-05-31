package ru.onalex.odashop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.onalex.odashop.models.ProfileRequest;
import ru.onalex.odashop.repositories.CustomerRepository;
import ru.onalex.odashop.services.CustomerService;

import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RequiredArgsConstructor
class OdaShopApplicationTests {

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @Test
    @SneakyThrows
    public void update_profile_valid_data_return_profile() {
        ProfileRequest profile = new ProfileRequest(); // нужно создать нормальный объект, пустой не катит :(
        AtomicReference<String> locationHeader = new AtomicReference<>("");
//        String newUsername = "megatron911@inbox.ru";
//        String newContactName = "Ракукин";
        String newAddress = "620555 Россия, г. Такойто, ул. Сякаято, д.1";
        String newPhone = "+7 999 888-77-66";
        String newComment = "какой-то комментарий";
//        profile.setUsername(newUsername);
//        profile.setContactName(newContactName);
        profile.setAddress(newAddress);
        profile.setPhone(newPhone);
        profile.setComment(newComment);
        int idForUpdate = 1;
        ProfileRequest updatedProfile = new ProfileRequest();
        mockMvc.perform(
                        put("/customer/profile",profile)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address",is(newAddress)))
                .andExpect(jsonPath("$.phone",is(newPhone)))
                .andExpect(jsonPath("$.comment",is(newComment)))
                //проверяем целостность неизмененных данных
                .andExpect(jsonPath("$.username",is(profile.getUsername())))
                .andExpect(jsonPath("$.contactName",is(profile.getContactName())));

        mockMvc.perform(
                        put("/customer/profile",new ProfileRequest())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedProfile)))
                .andExpect(status().isNotFound());
    }


}
