package com.aroa.sportifyme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
public class ParticipacionExistenteException extends RuntimeException {
    // Versión que acepta Long
    public ParticipacionExistenteException(Long usuarioId, Long desafioId) {
        super(String.format("El usuario %d ya está participando en el desafío %d", usuarioId, desafioId));
    }

    // Versión que acepta String (opcional)
    public ParticipacionExistenteException(String message) {
        super(message);
    }
}