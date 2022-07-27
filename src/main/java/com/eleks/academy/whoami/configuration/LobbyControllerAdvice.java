package com.eleks.academy.whoami.configuration;

import com.eleks.academy.whoami.controller.LobbyController;
import com.eleks.academy.whoami.core.exception.ErrorResponse;
import com.eleks.academy.whoami.db.exception.CreateLobbyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestControllerAdvice(assignableTypes = LobbyController.class)
public class LobbyControllerAdvice extends ResponseEntityExceptionHandler {

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

    @ExceptionHandler(CreateLobbyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleCreateLobbyException(CreateLobbyException e) {
        String message = e.getMessage();
        return ResponseEntity.badRequest().body(new ErrorResponse("Failed to create lobby", message == null ? null : List.of(message)));
    }
}
