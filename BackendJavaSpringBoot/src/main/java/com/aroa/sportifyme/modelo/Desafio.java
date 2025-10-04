package com.aroa.sportifyme.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "desafios")
public class Desafio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_actividad", nullable = false)
    private TipoActividad tipoActividad;

    // ✅ CORREGIDO: Cambiar Double por BigDecimal
    @Column(precision = 10, scale = 2)
    private BigDecimal objetivo;

    // ✅ CORREGIDO: Cambiar BigDecimal por String (para unidades como "km", "min", "kg")
    @Column(name = "unidad_objetivo", length = 20)
    private String unidadObjetivo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creador_id", nullable = false)
    private Usuario creador;

    @Column(name = "es_publico", nullable = false)
    private Boolean esPublico = true;

    @Column(name = "imagen_url", length = 255)
    private String imagenUrl;

    @Enumerated(EnumType.STRING)
    private DificultadDesafio dificultad;

    @Column(name = "max_participantes")
    private Integer maxParticipantes;
}