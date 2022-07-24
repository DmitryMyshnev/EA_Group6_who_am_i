package com.eleks.academy.whoami.db.mapper;

import com.eleks.academy.whoami.db.dto.CreateLobbyCommand;
import com.eleks.academy.whoami.db.dto.CreateLobbyCommandDto;
import com.eleks.academy.whoami.db.dto.LobbyDto;
import com.eleks.academy.whoami.db.dto.LobbyUserDto;
import com.eleks.academy.whoami.db.model.Lobby;
import com.eleks.academy.whoami.db.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LobbyMapper {

    @Mapping(target ="theme", source = "lobby.theme.name")
    LobbyDto toDto(Lobby lobby);

    LobbyUserDto toDto(User user);

    CreateLobbyCommand toModel(CreateLobbyCommandDto dto);
}
