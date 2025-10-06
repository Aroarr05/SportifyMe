package com.aroa.sportifyme.seguridad.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RankingDTO {
    private Long usuarioId;
    private String nombreUsuario;
    private BigDecimal valorActual; // ← CAMBIAR de 'progresoTotal' a 'valorActual'
    private String avatarUrl;

    // Constructor
    public RankingDTO(Long usuarioId, String nombreUsuario, BigDecimal valorActual, String avatarUrl) {
        this.usuarioId = usuarioId;
        this.nombreUsuario = nombreUsuario;
        this.valorActual = valorActual; // ← Ahora coincide con la BD
        this.avatarUrl = avatarUrl;
    }
}