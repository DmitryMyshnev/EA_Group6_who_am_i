package com.eleks.academy.whoami.db.exception;

public class ChangePasswordException extends RuntimeException {
    public ChangePasswordException() {
        super();
    }

    public ChangePasswordException(String message) {
        super(message);
    }
}