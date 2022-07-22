package com.eleks.academy.whoami.model.response;

import com.eleks.academy.whoami.core.GameState;
import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.model.dto.MessagesEntry;
import com.eleks.academy.whoami.model.dto.MessageHistory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDetails {

    private String id;

    private GameState status;

    private List<PlayersWithState> players;

    @JsonProperty("history")
    private MessageHistory history;

    public static GameDetails of(SynchronousGame game) {
        return GameDetails.builder()
                .id(game.getId())
                .status(game.getStatus())
                .players(game.getPlayersInGameWithState())
                .history(from(game))
                .build();
    }

    private static MessageHistory from(SynchronousGame game) {
        var entryDtoList = game
                .getHistory()
                .getEntries()
                .stream()
                .map(entry -> MessagesEntry.builder()
                        .id(entry.getId())
                        .playerId(entry.getPlayerId())
                        .playerName(entry.getPlayerName())
                        .playerQuestion(entry.getPlayerQuestion())
                        .answers(entry.getAnswers())
                        .build())
                .toList();
        return new MessageHistory(entryDtoList);
    }
}