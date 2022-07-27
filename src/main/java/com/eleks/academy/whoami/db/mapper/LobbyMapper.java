package com.eleks.academy.whoami.db.mapper;

import com.eleks.academy.whoami.db.dto.CreateLobbyCommand;
import com.eleks.academy.whoami.db.dto.CreateLobbyCommandDto;
import com.eleks.academy.whoami.db.dto.LobbyDto;
import com.eleks.academy.whoami.db.dto.LobbyUserDto;
import com.eleks.academy.whoami.db.dto.LobbyWithCountUsers;
import com.eleks.academy.whoami.db.model.Lobby;
import com.eleks.academy.whoami.db.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface LobbyMapper {

    @Mappings(value = {
            @Mapping(target = "theme", source = "lobby.theme.name"),
            @Mapping(target = "ownerUser", source = "lobby.user")
    })
    LobbyDto toDto(Lobby lobby);

    @Mappings(value = {
            @Mapping(target = "theme", source = "lobby.theme.name"),
            @Mapping(target = "ownerUser", source = "lobby.user")
    })
    LobbyWithCountUsers toDtoWithCountUser(Lobby lobby);

    LobbyUserDto toDto(User user);

    CreateLobbyCommand toModel(CreateLobbyCommandDto dto);
}
