package com.aroa.sportifyme.dto;

import lombok.Data;

@Data
public class RankingDTO {
    private Long usuarioId;
    private String nombreUsuario;
    private int posicion;
    private double valorActual;
    private String avatarUrl; // Opcional para la UI
}