package com.eleks.academy.whoami.db.exception;

public class TokenException extends RuntimeException {
    public TokenException() {
        super();
    }

    public TokenException(String message) {
        super(message);
    }
}
