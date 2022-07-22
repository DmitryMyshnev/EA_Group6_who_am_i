package com.eleks.academy.whoami.security.exception;

public class TokenRefreshException extends RuntimeException {
    public TokenRefreshException() {
        super();
    }

    public TokenRefreshException(String message) {
        super(message);
    }
}
