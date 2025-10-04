package com.aroa.sportifyme.seguridad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RankingDTO {
    private Long usuarioId;
    private String nombreUsuario;
    private BigDecimal progresoTotal; // ✅ Cambiar de Double a BigDecimal
    private String avatarUrl;

}