package com.eleks.academy.whoami.db.exception;

public class CreateUserException extends RuntimeException {

    public CreateUserException() {
        super();
    }

    public CreateUserException(String message) {
        super(message);
    }
}
