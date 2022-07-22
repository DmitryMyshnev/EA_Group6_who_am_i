package com.eleks.academy.whoami.core.impl;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Entry {

    private Integer id;
    private String playerId;
    private String playerName;
    private Boolean isGuess;
    private String playerQuestion;
    private List<AnsweringPlayer> answers = new ArrayList<>();

    public Entry(String playerId, String playerName, String playerQuestion, boolean isGuess) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.playerQuestion = playerQuestion;
        this.isGuess = isGuess;
    }

    public void addPlayerWithAnswer(AnsweringPlayer player) {
        this.answers.add(player);
    }
}
