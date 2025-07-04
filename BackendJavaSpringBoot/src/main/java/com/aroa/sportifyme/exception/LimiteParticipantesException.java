package com.aroa.sportifyme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // 403 Forbidden
public class LimiteParticipantesException extends RuntimeException {
    public LimiteParticipantesException(String message) {
        super(message);
    }

    public LimiteParticipantesException(String resourceName, int maxParticipantes) {
        super(String.format("El %s ha alcanzado el límite máximo de %d participantes",
                resourceName, maxParticipantes));
    }
}