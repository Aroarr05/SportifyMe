package com.aroa.sportifyme.seguridad.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RankingDTO {
    private UsuarioRanking usuario;
    private BigDecimal puntuacion;
    private Integer desafiosCompletados;
    private BigDecimal progresoTotal;
    private Integer posicion;

    // Sub-clase para el usuario
    @Data
    public static class UsuarioRanking {
        private Long id;
        private String nombre;
        private String avatarUrl;
        
        public UsuarioRanking(Long id, String nombre, String avatarUrl) {
            this.id = id;
            this.nombre = nombre;
            this.avatarUrl = avatarUrl;
        }
    }

    // Constructor para ranking simple (MAX(p.valorActual) → BigDecimal)
    public RankingDTO(Long usuarioId, String nombreUsuario, BigDecimal valorActual, String avatarUrl) {
        this.usuario = new UsuarioRanking(usuarioId, nombreUsuario, avatarUrl);
        this.puntuacion = valorActual != null ? valorActual : BigDecimal.ZERO;
        this.desafiosCompletados = 0;
        this.progresoTotal = BigDecimal.ZERO;
        this.posicion = 0;
    }

    // Constructor para ranking global (COUNT(...) → Long)
    public RankingDTO(Long usuarioId, String nombreUsuario, Long desafiosCompletadosCount, String avatarUrl) {
        this.usuario = new UsuarioRanking(usuarioId, nombreUsuario, avatarUrl);
        this.puntuacion = BigDecimal.valueOf(desafiosCompletadosCount != null ? desafiosCompletadosCount : 0L);
        this.desafiosCompletados = desafiosCompletadosCount != null ? desafiosCompletadosCount.intValue() : 0;
        this.progresoTotal = BigDecimal.ZERO;
        this.posicion = 0;
    }

    // Constructor vacío (importante para JPA)
    public RankingDTO() {
        // Constructor por defecto necesario
    }
}