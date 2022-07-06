package com.eleks.academy.whoami.core;

public enum GameState {
    WAITING_FOR_PLAYER("com.eleks.academy.whoami.core.state.WaitingForPlayers"),
    SUGGESTING_CHARACTER("com.eleks.academy.whoami.core.state.SuggestingCharacters"),
    PROCESSING_QUESTION("com.eleks.academy.whoami.core.state.ProcessingQuestion"),
    READY_TO_START("READY_TO_START"),
    FINISHED("FINISHED");

    private final String state;

    GameState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
