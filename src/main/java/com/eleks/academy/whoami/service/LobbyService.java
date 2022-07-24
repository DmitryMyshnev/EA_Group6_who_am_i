package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.db.dto.CreateLobbyCommand;
import com.eleks.academy.whoami.db.model.Lobby;
import com.eleks.academy.whoami.db.model.Theme;

import java.util.List;

public interface LobbyService {

    Lobby createLobby(CreateLobbyCommand command);

    List<Theme> findAllThemes();
}
