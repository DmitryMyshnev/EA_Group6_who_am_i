package com.eleks.academy.whoami.db.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LobbyWithCountUsers {
    private Long id;

    private String theme;

    private Integer numberOfPlayers;

    private Integer joinPlayers;

    private Boolean isPrivate;

    private LobbyUserDto ownerUser;
}
