package com.aroa.sportifyme.seguridad.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProgresoDTO {
    @NotNull
    @Positive
    private Double valorActual;

    @NotBlank
    private String unidad;

    private String comentario;

    @NotBlank
    private String dispositivo;

    @NotNull
    private Long desafioId;
}