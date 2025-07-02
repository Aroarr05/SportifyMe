package com.aroa.sportifyme.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "participaciones",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"usuario_id", "desafio_id"}))
public class Participacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desafio_id", nullable = false)
    private Desafio desafio;

    @Column(name = "fecha_union", nullable = false, updatable = false)
    private LocalDateTime fechaUnion = LocalDateTime.now();

    // Constructor adicional para facilitar la creación
    public Participacion(Usuario usuario, Desafio desafio) {
        this.usuario = usuario;
        this.desafio = desafio;
    }

    public Participacion() {}
}