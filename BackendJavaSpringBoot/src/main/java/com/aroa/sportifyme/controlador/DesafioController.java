package com.aroa.sportifyme.controlador;

import com.aroa.sportifyme.modelo.Desafio;
import com.aroa.sportifyme.servicio.DesafioServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/desafios")
public class DesafioController {

    private final DesafioServicio desafioServicio;

    // Inyección por constructor (recomendado)
    public DesafioController(DesafioServicio desafioServicio) {
        this.desafioServicio = desafioServicio;
    }

    @GetMapping
    public List<Desafio> listarDesafios() {
        return desafioServicio.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Desafio> crearDesafio(@RequestBody Desafio desafio) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(desafioServicio.crearDesafio(desafio));
    }
}