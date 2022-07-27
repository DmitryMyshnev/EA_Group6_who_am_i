package com.eleks.academy.whoami.db.exception;

public class NotMatchesPasswordException extends RuntimeException {
    public NotMatchesPasswordException() {
        super();
    }

    public NotMatchesPasswordException(String message) {
        super(message);
    }
}
