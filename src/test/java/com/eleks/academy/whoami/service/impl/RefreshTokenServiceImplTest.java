package com.eleks.academy.whoami.service.impl;

import com.eleks.academy.whoami.db.model.RefreshToken;
import com.eleks.academy.whoami.db.model.User;
import com.eleks.academy.whoami.repository.RefreshTokenRepository;
import com.eleks.academy.whoami.security.exception.NotAcceptableOauthException;
import com.eleks.academy.whoami.security.jwt.Jwt;
import com.eleks.academy.whoami.service.RefreshTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class RefreshTokenServiceImplTest {

    private RefreshTokenService refreshTokenService;
    private Jwt jwt;
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void init() {
        jwt = Mockito.mock(Jwt.class);
        refreshTokenRepository = Mockito.mock(RefreshTokenRepository.class);
        refreshTokenService = new RefreshTokenServiceImpl(refreshTokenRepository, jwt);
    }

    @Test
    void createRefreshTokenShouldByReturnNewToken() {
        var user = User.builder()
                .id(1L)
                .email("mail@com")
                .build();
        var refreshToken = new RefreshToken();
        refreshToken.setToken("123");
        refreshToken.setUser(user);

        when(jwt.generateToken(anyString(), any(Long.class))).thenReturn("123");
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);
        var newToken = refreshTokenService.createRefreshToken(user);

        Assertions.assertEquals("123", newToken.getToken());
    }

    @Test
    void verifyTokenShouldBeReturnTrue() {
        var refreshToken = new RefreshToken();
        refreshToken.setToken("123");

        when(jwt.validateJwtToken(anyString())).thenReturn(true);
        var verifyToken = refreshTokenService.verifyToken(refreshToken);

        Assertions.assertTrue(verifyToken);
    }


    @Test
    void verifyTokenThrowException() {
        var refreshToken = new RefreshToken();
        refreshToken.setToken("123");

        when(jwt.validateJwtToken(anyString())).thenThrow(NotAcceptableOauthException.class);

        Assertions.assertThrows(NotAcceptableOauthException.class, ()->refreshTokenService.verifyToken(refreshToken));
    }
}
