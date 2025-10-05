package com.aroa.sportifyme.controlador;

import com.aroa.sportifyme.modelo.Desafio;
import com.aroa.sportifyme.servicio.DesafioServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/desafios")
@CrossOrigin(origins = "http://localhost:4200") // ✅ AÑADIR CORS
public class DesafioController {

    private final DesafioServicio desafioServicio;

    public DesafioController(DesafioServicio desafioServicio) {
        this.desafioServicio = desafioServicio;
    }

    @GetMapping
    public ResponseEntity<?> listarDesafios() { // ✅ Cambiado a ResponseEntity<?>
        try {
            List<Desafio> desafios = desafioServicio.listarTodos();
            return ResponseEntity.ok(desafios);
        } catch (Exception e) {
            e.printStackTrace(); // ✅ Para ver el error en consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener desafíos: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crearDesafio( // ✅ Cambiado a ResponseEntity<?>
            @RequestBody Desafio desafio,
            @RequestHeader("X-User-ID") Long creadorId) {
        
        try {
            Desafio nuevoDesafio = desafioServicio.crearDesafio(desafio, creadorId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDesafio);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear desafío: " + e.getMessage());
        }
    }
}