package com.eleks.academy.whoami.model.response;

import com.eleks.academy.whoami.core.GameState;
import com.eleks.academy.whoami.core.SynchronousGame;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameLight {
    private String id;
    private GameState status;
    private String playersInGame;

    public static GameLight of(SynchronousGame game) {
        return GameLight.builder()
                .id(game.getId())
                .status(game.getStatus())
                .playersInGame(game.getCountPlayersInGame().toString())
                .build();
    }
}
