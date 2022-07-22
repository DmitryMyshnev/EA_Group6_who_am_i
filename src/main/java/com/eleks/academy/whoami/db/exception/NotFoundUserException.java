package com.eleks.academy.whoami.db.exception;

public class NotFoundUserException  extends RuntimeException{
    public NotFoundUserException() {
        super();
    }

    public NotFoundUserException(String message) {
        super(message);
    }
}
