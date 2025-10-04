package com.aroa.sportifyme.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "progresos")
public class Progreso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ CORREGIDO: Cambiar Double por BigDecimal
    @Column(name = "valor_actual", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorActual;

    @Column(nullable = false, length = 20)
    private String unidad;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Column(length = 50)
    private String dispositivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desafio_id", nullable = false)
    private Desafio desafio;

    @PrePersist
    @PreUpdate
    private void validar() {
        if (!this.unidad.equals(this.desafio.getUnidadObjetivo())) {
            throw new IllegalArgumentException("Unidad no coincide con el desafío");
        }
        // ✅ CORREGIDO: Validación para BigDecimal
        if (this.valorActual.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El valor debe ser positivo");
        }
    }
}