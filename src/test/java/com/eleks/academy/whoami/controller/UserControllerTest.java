package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.db.exception.CreateUserException;
import com.eleks.academy.whoami.db.mapper.UserMapper;
import com.eleks.academy.whoami.db.model.User;
import com.eleks.academy.whoami.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {UserController.class, UserMapper.class})
class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void createUser() throws Exception{
        when(userService.save(any(String.class))).thenThrow(CreateUserException.class);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("som json content"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void confirmRegistration() throws Exception {

        var user = User.builder()
                .id(1L)
                .name("Pol")
                .email("mail@test.com")
                .password("12345678")
                .isActivated(true)
                .build();
      when(userService.save(any(String.class))).thenReturn(user);

        mockMvc.perform(get("/users/confirm")
                        .param("token", "some token"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Pol"))
                .andExpect(jsonPath("$.email").value("mail@test.com"))
                .andExpect(jsonPath("$.isActivated").value(true));
    }
}