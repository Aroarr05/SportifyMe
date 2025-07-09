package com.aroa.sportifyme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*@ResponseStatus(HttpStatus.NOT_FOUND)
public class ParticipacionNoEncontradaException extends RuntimeException {
    public ParticipacionNoEncontradaException(Long id) {
        super("No se encontró la participación con ID: " + id);
    }
}*/

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ParticipacionNoEncontradaException extends RuntimeException {
    public ParticipacionNoEncontradaException(Long usuarioId, Long desafioId) {
        super("El usuario " + usuarioId + " no participa en el desafío " + desafioId);
    }
}