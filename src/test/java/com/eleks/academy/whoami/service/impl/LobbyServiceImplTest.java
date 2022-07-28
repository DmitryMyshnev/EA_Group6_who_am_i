package com.eleks.academy.whoami.service.impl;

import com.eleks.academy.whoami.db.dto.CreateLobbyCommand;
import com.eleks.academy.whoami.db.exception.CreateLobbyException;
import com.eleks.academy.whoami.db.model.Theme;
import com.eleks.academy.whoami.db.model.User;
import com.eleks.academy.whoami.repository.LobbyAndUserRepository;
import com.eleks.academy.whoami.repository.LobbyRepository;
import com.eleks.academy.whoami.repository.ThemeRepository;
import com.eleks.academy.whoami.repository.UserRepository;
import com.eleks.academy.whoami.service.LobbyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class LobbyServiceImplTest {


    private ThemeRepository themeRepository;
    private UserRepository userRepository;
    private LobbyService lobbyService;

    @BeforeEach
    void init() {
        LobbyRepository  lobbyRepository = Mockito.mock(LobbyRepository.class);
        themeRepository = Mockito.mock(ThemeRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder   encoder = Mockito.mock(PasswordEncoder.class);
        LobbyAndUserRepository lobbyAndUserRepository = Mockito.mock(LobbyAndUserRepository.class);
        lobbyService = new LobbyServiceImpl(lobbyRepository, themeRepository, userRepository, encoder, lobbyAndUserRepository);
    }

    @Test
    void givenNotExistTheme_createLobby_shouldByThroeException() {
        when(themeRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        var createLobbyCommand = CreateLobbyCommand.builder()
                .theme("test")
                .build();
        Assertions.assertThrows(CreateLobbyException.class, () -> lobbyService.createLobby(createLobbyCommand));
    }

    @Test
    void givenNullPassword_createLobby_shouldByThroeException() {
        var createLobbyCommand = CreateLobbyCommand.builder()
                .userId(1L)
                .isPrivate(true)
                .password(null)
                .build();
        when(themeRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(new Theme()));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(CreateLobbyException.class, () -> lobbyService.createLobby(createLobbyCommand));
    }

}