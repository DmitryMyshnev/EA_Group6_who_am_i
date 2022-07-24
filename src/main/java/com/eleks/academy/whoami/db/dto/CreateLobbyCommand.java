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
public class CreateLobbyCommand {

    private String theme;

    private Integer numberOfPlayers;

    private Boolean isPrivate;

    private String password;

    private Long userId;

    public Boolean isPrivate() {
        return isPrivate;
    }
}
