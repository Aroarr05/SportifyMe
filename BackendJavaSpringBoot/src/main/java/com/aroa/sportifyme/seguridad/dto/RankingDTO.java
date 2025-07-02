package com.aroa.sportifyme.seguridad.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RankingDTO {
    private String nombreUsuario;
    private double promedioMinutosPorKm;
    private double distanciaTotal;
    private int posicion; // Se calcula en el frontend o servicio
}
