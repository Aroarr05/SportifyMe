package com.aroa.sportifyme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
public class DesafioExpiradoException extends RuntimeException {
    public DesafioExpiradoException(String message) {
        super(message);
    }

    public DesafioExpiradoException(String message, Throwable cause) {
        super(message, cause);
    }
}