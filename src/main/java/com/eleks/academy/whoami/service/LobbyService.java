package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.db.dto.CreateLobbyCommand;
import com.eleks.academy.whoami.db.model.Lobby;
import com.eleks.academy.whoami.db.model.Theme;
import com.eleks.academy.whoami.db.model.User;

import java.util.List;

public interface LobbyService {

    Lobby createLobby(CreateLobbyCommand command);

    List<Theme> findAllThemes();

    List<User> findAllUsersByLobbyId(Long lobbyId);

    List<Lobby> findAllLobbies();

    int countJoinPlayers(Long id);
}
