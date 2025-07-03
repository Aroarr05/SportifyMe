package com.aroa.sportifyme.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usuario_logros",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "logro_id"}))
public class UsuarioLogro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logro_id", nullable = false)
    private Logro logro;

    @Column(name = "fecha_obtencion", nullable = false)
    private LocalDateTime fechaObtencion = LocalDateTime.now();

    public UsuarioLogro() {}

    public UsuarioLogro(Usuario usuario, Logro logro) {
        this.usuario = usuario;
        this.logro = logro;
    }
}