package com.eleks.academy.whoami.security.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthExceptionResponse {

    private String message;

    private String details;
}
