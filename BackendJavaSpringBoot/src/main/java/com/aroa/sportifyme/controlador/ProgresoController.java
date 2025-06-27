package com.aroa.sportifyme.controlador;

import com.aroa.sportifyme.modelo.Progreso;
import com.aroa.sportifyme.servicio.ProgresoServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/progresos")
public class ProgresoController {

    private final ProgresoServicio progresoService;

    public ProgresoController(ProgresoServicio progresoService) {
        this.progresoService = progresoService;
    }

    @PostMapping
    public ResponseEntity<Progreso> registrarProgreso(@RequestBody Progreso progreso) {
        Progreso nuevoProgreso = progresoService.registrarProgreso(progreso);
        return ResponseEntity
                .created(URI.create("/api/progresos/" + nuevoProgreso.getId()))
                .body(nuevoProgreso);
    }

    @GetMapping("/desafio/{desafioId}")
    public List<Progreso> obtenerProgresosPorDesafio(@PathVariable Long desafioId) {
        return progresoService.obtenerProgresosPorDesafio(desafioId);
    }

    @GetMapping("/ranking/{desafioId}")
    public List<Progreso> obtenerRankingDesafio(@PathVariable Long desafioId) {
        return progresoService.obtenerRankingDesafio(desafioId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Progreso> buscarProgresoPorId(@PathVariable Long id) {
        return progresoService.buscarProgresoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}