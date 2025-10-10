package com.aroa.sportifyme.seguridad.dto;

import com.aroa.sportifyme.modelo.Desafio;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DesafioDTO(
        Long id,  // Agregar ID
        String titulo,
        String descripcion,
        Desafio.TipoActividad tipoActividad,  // Usar el enum de Desafio
        BigDecimal objetivo,  // Cambiar de double a BigDecimal
        String unidadObjetivo,  // Agregar este campo que falta
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin,
        Long creadorId,
        Boolean esPublico,  // Agregar este campo
        String imagenUrl,  // Agregar este campo
        Desafio.Dificultad dificultad,  // Agregar este campo
        Integer maxParticipantes  // Agregar este campo
) {
    
    // Constructor desde la entidad Desafio
    public static DesafioDTO fromEntity(Desafio desafio) {
        return new DesafioDTO(
                desafio.getId(),
                desafio.getTitulo(),
                desafio.getDescripcion(),
                desafio.getTipoActividad(),
                desafio.getObjetivo(),
                desafio.getUnidadObjetivo(),
                desafio.getFechaInicio(),
                desafio.getFechaFin(),
                desafio.getCreador() != null ? desafio.getCreador().getId() : null,
                desafio.getEsPublico(),
                desafio.getImagenUrl(),
                desafio.getDificultad(),
                desafio.getMaxParticipantes()
        );
    }
}