package com.eleks.academy.whoami.service.impl;

import com.eleks.academy.whoami.db.dto.CreateLobbyCommand;
import com.eleks.academy.whoami.db.exception.CreateLobbyException;
import com.eleks.academy.whoami.db.model.Lobby;
import com.eleks.academy.whoami.db.model.LobbyAndUser;
import com.eleks.academy.whoami.db.model.Theme;
import com.eleks.academy.whoami.db.model.User;
import com.eleks.academy.whoami.repository.LobbyAndUserRepository;
import com.eleks.academy.whoami.repository.LobbyRepository;
import com.eleks.academy.whoami.repository.ThemeRepository;
import com.eleks.academy.whoami.repository.UserRepository;
import com.eleks.academy.whoami.service.LobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.Boolean.TRUE;

@RequiredArgsConstructor
@Service
public class LobbyServiceImpl implements LobbyService {

    private final LobbyRepository lobbyRepository;
    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final LobbyAndUserRepository lobbyAndUserRepository;

    @Override
    @Transactional
    public Lobby createLobby(CreateLobbyCommand command) {
        return themeRepository.findByName(command.getTheme())
                .map(theme -> userRepository.findById(command.getUserId())
                        .map(user -> {
                            var lobby = Lobby.builder()
                                    .theme(theme)
                                    .numberOfPlayers(command.getNumberOfPlayers())
                                    .user(user)
                                    .isPrivate(command.isPrivate())
                                    .build();
                            if (TRUE.equals(command.isPrivate())) {
                                if (command.getPassword() == null) {
                                    throw new CreateLobbyException("Password must be not null for private lobby");
                                }
                                lobby.setPassword(encoder.encode(command.getPassword()));
                            }
                            var lobbyEntity = lobbyRepository.save(lobby);
                            var lobbyAndUser = LobbyAndUser.builder()
                                    .lobbyId(lobbyEntity.getId())
                                    .userId(user.getId())
                                    .build();
                            lobbyAndUserRepository.save(lobbyAndUser);
                            return lobbyEntity;
                        })
                        .orElseThrow(() -> new CreateLobbyException("User is not found")))
                .orElseThrow(() -> new CreateLobbyException("Theme '" + command.getTheme() + "' is not found"));
    }

    @Override
    @Transactional
    public List<Theme> findAllThemes() {
        return themeRepository.findAll();
    }

    @Override
    @Transactional
    public List<User> findAllUsersByLobbyId(Long lobbyId) {
        return lobbyRepository.findAllUsersByLobbyId(lobbyId);
    }

    @Override
    public List<Lobby> findAllLobbies() {
       return   lobbyRepository.findAll();
    }

    @Override
    public int countJoinPlayers(Long lobbyId) {
      return   lobbyAndUserRepository.countByLobbyId(lobbyId);
    }
}
