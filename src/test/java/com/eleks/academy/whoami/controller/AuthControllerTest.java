package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.db.dto.CredentialRequest;
import com.eleks.academy.whoami.db.dto.JwtResponse;
import com.eleks.academy.whoami.db.dto.RefreshTokenCommandDto;
import com.eleks.academy.whoami.db.dto.RefreshTokenResponse;
import com.eleks.academy.whoami.service.AuthService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@TestPropertySource(properties = {"spring.mail.username = test@mail", "spring.mail.password = 123", "jwt.token-secret = secret"})
class AuthControllerTest {
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;


    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void signIn() throws Exception {
        var jwtResponse = JwtResponse.builder()
                .userId(1L)
                .email("test@com")
                .token("12345")
                .refreshToken("456")
                .build();
        when(authService.authenticate(any(CredentialRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"email\" : \"test@com\"," +
                                "\"password\" : \"123455678\"}"))
                .andExpect(jsonPath("$.email").value("test@com"))
                .andExpect(jsonPath("$.token").value("12345"))
                .andExpect(jsonPath("$.refreshToken").value("456"));
    }

    @Test
    void refreshToken() throws Exception {
        var tokenResponse = new RefreshTokenResponse("12345");

        when(authService.refreshToken(any(RefreshTokenCommandDto.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"refreshToken\" : \"456\"" +
                              "}"))
                .andExpect(jsonPath("$.token").value("12345"))
                .andExpect(jsonPath("$.type").value("Bearer"));
    }
}