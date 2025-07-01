package com.aroa.sportifyme.seguridad.dto;

import com.aroa.sportifyme.modelo.TipoActividad;

import java.time.LocalDateTime;

public record DesafioDTO(
        String titulo,
        String descripcion,
        TipoActividad tipoActividad,
        double objetivo,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin,
        Long creadorId
) {}