package com.aroa.sportifyme.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "progresos")
public class Progreso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desafio_id", nullable = false)
    private Desafio desafio;

    @Column(name = "valor_actual", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorActual;

    @Column(name = "unidad", nullable = false, length = 20)
    private String unidad;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;

    @Column(name = "dispositivo", length = 50)
    private String dispositivo;

    @PrePersist
    @PreUpdate
    private void validar() {
        if (desafio != null && !this.unidad.equals(desafio.getUnidadObjetivo())) {
            throw new IllegalArgumentException("La unidad del progreso no coincide con la unidad del desafío");
        }
        if (this.valorActual.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El valor actual debe ser positivo");
        }
    }

    // Constructor por defecto
    public Progreso() {}

    // Constructor con parámetros principales
    public Progreso(Usuario usuario, Desafio desafio, BigDecimal valorActual, String unidad) {
        this.usuario = usuario;
        this.desafio = desafio;
        this.valorActual = valorActual;
        this.unidad = unidad;
        this.fechaRegistro = LocalDateTime.now();
    }

    // Método helper para calcular el porcentaje de completado del desafío
    public BigDecimal calcularPorcentajeCompletado() {
        if (desafio == null || desafio.getObjetivo() == null || desafio.getObjetivo().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        // ✅ CORREGIDO: Usando RoundingMode.HALF_UP en lugar del deprecated
        return valorActual.divide(desafio.getObjetivo(), 4, RoundingMode.HALF_UP)
                         .multiply(BigDecimal.valueOf(100))
                         .min(BigDecimal.valueOf(100)); // No más del 100%
    }

    // Método para verificar si el desafío está completado
    public boolean estaCompletado() {
        if (desafio == null || desafio.getObjetivo() == null) {
            return false;
        }
        return valorActual.compareTo(desafio.getObjetivo()) >= 0;
    }
}