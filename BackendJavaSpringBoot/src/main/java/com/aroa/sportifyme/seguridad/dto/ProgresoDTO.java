package com.aroa.sportifyme.seguridad.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProgresoDTO {
    @Positive(message = "La distancia debe ser positiva")
    private double distancia; // en kilómetros

    @Positive(message = "El tiempo debe ser positivo")
    private long minutos; // duración en minutos
}
