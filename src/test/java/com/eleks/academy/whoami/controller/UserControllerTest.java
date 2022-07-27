package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.db.exception.CreateUserException;
import com.eleks.academy.whoami.db.model.User;
import com.eleks.academy.whoami.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(properties = {"spring.mail.username = test@mail", "spring.mail.password = 123", "jwt.token-secret = secret", "confirm-url = localhost:8080"})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

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

    @Test
    void givenNullEmail_sendMailRestorePassword_shouldBeThrowException() throws Exception{

        mockMvc.perform(get("/users/password-restore")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\" : null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed!"));
    }
}