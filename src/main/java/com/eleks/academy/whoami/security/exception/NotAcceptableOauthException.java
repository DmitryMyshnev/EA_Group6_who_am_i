package com.eleks.academy.whoami.security.exception;

public class NotAcceptableOauthException extends RuntimeException {

    public NotAcceptableOauthException() {
        super();
    }

    public NotAcceptableOauthException(String message) {
        super(message);
    }
}
