package com.aroa.sportifyme.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "desafios")
public class Desafio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(length = 500)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('correr', 'ciclismo', 'natación', 'gimnasio')")
    private TipoActividad tipoActividad;

    @Column(nullable = false, precision = 10, scale = 2)
    private Double objetivo;

    @Column(nullable = false, length = 20)
    private String unidadObjetivo;  // Nuevo campo para coincidir con la tabla

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaFin;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean esPublico = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creador_id", nullable = false)
    private Usuario creador;

    @OneToMany(mappedBy = "desafio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Progreso> progresos = new ArrayList<>();

    public void agregarProgreso(Progreso progreso) {
        progresos.add(progreso);
        progreso.setDesafio(this);
    }
}