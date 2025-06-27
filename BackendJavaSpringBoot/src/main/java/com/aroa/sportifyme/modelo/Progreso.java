package com.aroa.sportifyme.modelo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "progresos")
public class Progreso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double valorActual;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(length = 500)
    private String comentario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desafio_id", nullable = false)
    private Desafio desafio;

    // Constructores
    public Progreso() {
        this.fechaRegistro = LocalDateTime.now();
    }

    public Progreso(Double valorActual, Usuario usuario, Desafio desafio) {
        this();
        this.valorActual = valorActual;
        this.usuario = usuario;
        this.desafio = desafio;
    }
}