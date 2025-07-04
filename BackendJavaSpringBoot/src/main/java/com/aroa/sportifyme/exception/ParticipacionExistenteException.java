package com.aroa.sportifyme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
public class ParticipacionExistenteException extends RuntimeException {
    public ParticipacionExistenteException(String message) {
        super(message);
    }

    public ParticipacionExistenteException(String message, Throwable cause) {
        super(message, cause);
    }
}