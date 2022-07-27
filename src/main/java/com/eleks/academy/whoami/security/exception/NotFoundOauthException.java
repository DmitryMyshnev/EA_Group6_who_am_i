package com.eleks.academy.whoami.security.exception;

public class NotFoundOauthException extends RuntimeException {
    public NotFoundOauthException() {
        super();
    }

    public NotFoundOauthException(String message) {
        super(message);
    }
}
