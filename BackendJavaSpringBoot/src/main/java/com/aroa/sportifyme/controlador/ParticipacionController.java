package com.aroa.sportifyme.controlador;

import com.aroa.sportifyme.modelo.Participacion;
import com.aroa.sportifyme.servicio.ParticipacionServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/participaciones")
public class ParticipacionController {

    private final ParticipacionServicio participacionServicio;

    public ParticipacionController(ParticipacionServicio participacionServicio) {
        this.participacionServicio = participacionServicio;
    }

    @PostMapping
    public ResponseEntity<Participacion> unirseADesafio(@RequestBody Participacion participacion) {
        Participacion nuevaParticipacion = participacionServicio.unirseADesafio(participacion);
        return ResponseEntity.ok(nuevaParticipacion);
    }

    @GetMapping("/verificar")
    public boolean verificarParticipacion(
            @RequestParam Long usuarioId,
            @RequestParam Long desafioId) {
        return participacionServicio.usuarioParticipaEnDesafio(usuarioId, desafioId);
    }
}