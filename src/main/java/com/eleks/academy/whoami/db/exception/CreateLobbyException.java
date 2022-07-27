package com.eleks.academy.whoami.db.exception;

public class CreateLobbyException extends RuntimeException {
    public CreateLobbyException() {
        super();
    }

    public CreateLobbyException(String message) {
        super(message);
    }
}
