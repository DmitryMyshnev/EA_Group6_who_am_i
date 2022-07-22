package com.eleks.academy.whoami.model.dto;

import com.eleks.academy.whoami.core.impl.AnsweringPlayer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessagesEntry {
    private Integer id;

    private String playerId;

    private String playerName;

    private String playerQuestion;

    private Boolean isGuess;

    private List<AnsweringPlayer> answers = new ArrayList<>();
}
