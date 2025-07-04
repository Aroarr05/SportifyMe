package com.aroa.sportifyme.exception;

public class ParticipacionNoEncontradaException extends RuntimeException {
    public ParticipacionNoEncontradaException(Long usuarioId, Long desafioId) {
        super("El usuario " + usuarioId + " no participa en el desafío " + desafioId);
    }
}