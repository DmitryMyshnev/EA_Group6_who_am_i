package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.db.dto.CreateLobbyCommand;
import com.eleks.academy.whoami.db.dto.LobbyFilter;
import com.eleks.academy.whoami.db.model.Lobby;
import com.eleks.academy.whoami.db.model.Theme;
import com.eleks.academy.whoami.db.model.User;

import java.util.List;
import java.util.stream.Stream;

public interface LobbyService {

    Lobby createLobby(CreateLobbyCommand command);

    List<Theme> findAllThemes();

    List<User> findAllUsersByLobbyId(Long lobbyId);

    List<Lobby> findAllLobbies();

    List<Lobby> filter(LobbyFilter lobbyFilter);

    Stream<Long> findAllLobbyIdsWithJoinUser();

    Stream<Long> findAllLobbyIdsWithJoinUserIn(List<Long> ids);
}
