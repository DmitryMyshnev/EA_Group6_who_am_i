package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.db.dto.CreateLobbyCommand;
import com.eleks.academy.whoami.db.model.Lobby;

public interface LobbyService {

    Lobby createLobby(CreateLobbyCommand command);

}
