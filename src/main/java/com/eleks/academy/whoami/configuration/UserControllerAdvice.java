package com.eleks.academy.whoami.configuration;

import com.eleks.academy.whoami.controller.UserController;
import com.eleks.academy.whoami.core.exception.ErrorResponse;
import com.eleks.academy.whoami.db.exception.CreateUserException;
import com.eleks.academy.whoami.db.exception.TokenException;
import com.eleks.academy.whoami.db.exception.NotFoundUserException;
import com.eleks.academy.whoami.db.exception.NotMatchesPasswordException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@RestControllerAdvice(assignableTypes = UserController.class)
public class UserControllerAdvice extends ResponseEntityExceptionHandler {

    public static final String FAILED_CREATE_ACCOUNT = "Failed to create a user";
    public static final String FAILED_SEND_MAIL = "Failed to send a mail";
    public static final String FAILED_RESTORE_PASSWORD = "Failed to restore password";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(collectingAndThen(
                        toList(),
                        details -> ResponseEntity.badRequest()
                                .body(new ErrorResponse("Validation failed!", details))
                ));
    }

    @ExceptionHandler(CreateUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleCreateUserException(CreateUserException e) {
        String message = e.getMessage();
        return ResponseEntity.badRequest().body(new ErrorResponse(FAILED_CREATE_ACCOUNT, message == null ? null : List.of(message)));
    }


    @ExceptionHandler(MailSendException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleSendMailException(MailSendException e) {
        String message = e.getMessage();
        return ResponseEntity.internalServerError().body(new ErrorResponse(FAILED_SEND_MAIL, message == null ? null : List.of(message)));
    }


    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<Object> handleNotFoundUserException(NotFoundUserException e) {
        String message = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(FAILED_RESTORE_PASSWORD, message == null ? null : List.of(message)));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<Object> handleExpirationTokenException(TokenException e) {
        String message = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorResponse(FAILED_RESTORE_PASSWORD, message == null ? null : List.of(message)));
    }

    @ExceptionHandler(NotMatchesPasswordException.class)
    public ResponseEntity<Object> handleNotMatchesPasswordException(NotMatchesPasswordException e) {
        String message = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorResponse(FAILED_RESTORE_PASSWORD, message == null ? null : List.of(message)));
    }
}
