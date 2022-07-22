package com.eleks.academy.whoami.security.exception;

import com.eleks.academy.whoami.controller.AuthController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = AuthController.class)
public class OauthControllerAdvice {
    public static final String FAILED_AUTHENTICATION = "Failed to authentication";

    @ExceptionHandler(NotFoundOauthException.class)
    public ResponseEntity<Object> handleNotFoundOauthException(NotFoundOauthException e) {
        String message = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthExceptionResponse(FAILED_AUTHENTICATION, message == null ? null : e.getMessage()));
    }

    @ExceptionHandler(NotAcceptableOauthException.class)
    public ResponseEntity<Object> handleNotAcceptableException(NotAcceptableOauthException e) {
        String message = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new AuthExceptionResponse(FAILED_AUTHENTICATION, message == null ? null : e.getMessage()));
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<Object> handleTokenRefreshException(TokenRefreshException e) {
        String message = e.getMessage();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthExceptionResponse(FAILED_AUTHENTICATION, message == null ? null : e.getMessage()));
    }
}
