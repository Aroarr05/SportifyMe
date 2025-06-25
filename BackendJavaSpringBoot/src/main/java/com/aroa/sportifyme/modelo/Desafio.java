package com.aroa.sportifyme.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "desafios")
public class Desafio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;
    private String tipoActividad;  // Ej: "correr", "ciclismo"
    private Double objetivo;       // Ej: 5.0 (km o minutos)

    @ManyToOne
    @JoinColumn(name = "creador_id")
    private Usuario creador;

}