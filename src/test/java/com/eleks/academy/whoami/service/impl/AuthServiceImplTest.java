package com.eleks.academy.whoami.service.impl;

import com.eleks.academy.whoami.db.dto.CredentialRequest;
import com.eleks.academy.whoami.db.dto.RefreshTokenCommandDto;
import com.eleks.academy.whoami.db.model.RefreshToken;
import com.eleks.academy.whoami.db.model.User;
import com.eleks.academy.whoami.repository.RefreshTokenRepository;
import com.eleks.academy.whoami.security.TokenBlackList;
import com.eleks.academy.whoami.security.jwt.Jwt;
import com.eleks.academy.whoami.service.AuthService;
import com.eleks.academy.whoami.service.RefreshTokenService;
import com.eleks.academy.whoami.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {
    private UserService userService;
    private RefreshTokenService refreshTokenService;
    private Jwt jwt;
    private RefreshTokenRepository refreshTokenRepository;
    private AuthService authService;

    @BeforeEach
    void init() {
        userService = Mockito.mock(UserService.class);
        refreshTokenService = Mockito.mock(RefreshTokenService.class);
        jwt = Mockito.mock(Jwt.class);
        refreshTokenRepository = Mockito.mock(RefreshTokenRepository.class);
        TokenBlackList tokenBlackList = Mockito.mock(TokenBlackList.class);
        authService = new AuthServiceImpl(userService, refreshTokenService, jwt, refreshTokenRepository, tokenBlackList);
    }

    @Test
    void authenticate() {
        var refreshToken = new RefreshToken();
        refreshToken.setToken("456");
        var user = User.builder()
                .email("test@com")
                .isActivated(true)
                .password("$2a$10$IUsLbzWq55njBJQpEtMK9ucNH.FjjVyeCWZYMEzZ9ToZ8YLTpE8p.")
                .build();

        when(userService.loadUserByUsername(anyString())).thenReturn(user);
        when(jwt.generateToken(anyString(), any(Long.class))).thenReturn("123");
        when(refreshTokenService.createRefreshToken(any(User.class))).thenReturn(refreshToken);
        var authenticate = authService.authenticate(new CredentialRequest("test@aom","12345678"));

        Assertions.assertEquals("123", authenticate.getToken());
        Assertions.assertEquals("456", authenticate.getRefreshToken());
    }

    @Test
    void refreshToken() {
        var user = User.builder()
                .email("test@com")
                .isActivated(true)
                .password("$2a$10$IUsLbzWq55njBJQpEtMK9ucNH.FjjVyeCWZYMEzZ9ToZ8YLTpE8p.")
                .build();
        var refreshToken = new RefreshToken();
        refreshToken.setToken("456");
        refreshToken.setUser(user);

        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(refreshToken));
        when(refreshTokenService.verifyToken(any(RefreshToken.class))).thenReturn(true);
        when(jwt.generateToken(anyString(), any(Long.class))).thenReturn("123");
        var refreshTokenResponse = authService.refreshToken(new RefreshTokenCommandDto("123"));

        Assertions.assertEquals("123", refreshTokenResponse.getToken());

    }

}
