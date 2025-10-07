package com.aroa.sportifyme.controlador;

import com.aroa.sportifyme.modelo.Desafio;
import com.aroa.sportifyme.seguridad.dto.request.DesafioRequest;
import com.aroa.sportifyme.servicio.DesafioServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/desafios")
@CrossOrigin(origins = "http://localhost:4200")
public class DesafioController {

    private final DesafioServicio desafioServicio;

    public DesafioController(DesafioServicio desafioServicio) {
        this.desafioServicio = desafioServicio;
    }

    @GetMapping
    public ResponseEntity<?> listarDesafios() {
        try {
            List<Desafio> desafios = desafioServicio.listarTodos();
            return ResponseEntity.ok(desafios);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener desafíos: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crearDesafio(
            @RequestBody DesafioRequest desafioRequest, // ✅ Cambiado a DesafioRequest
            @RequestHeader("X-User-ID") Long creadorId) {
        
        try {
            Desafio nuevoDesafio = desafioServicio.crearDesafio(desafioRequest, creadorId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDesafio);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear desafío: " + e.getMessage());
        }
    }

    // ✅ NUEVOS ENDPOINTS que coinciden con tu servicio
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDesafioPorId(@PathVariable Long id) {
        try {
            Desafio desafio = desafioServicio.buscarPorId(id);
            return ResponseEntity.ok(desafio);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Desafío no encontrado: " + e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarDesafiosPorCreador(@PathVariable Long usuarioId) {
        try {
            List<Desafio> desafios = desafioServicio.listarPorCreador(usuarioId);
            return ResponseEntity.ok(desafios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener desafíos del usuario: " + e.getMessage());
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<?> listarDesafiosActivos() {
        try {
            List<Desafio> desafios = desafioServicio.listarDesafiosActivos();
            return ResponseEntity.ok(desafios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener desafíos activos: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDesafio(
            @PathVariable Long id,
            @RequestBody DesafioRequest desafioRequest, // ✅ Cambiado a DesafioRequest
            @RequestHeader("X-User-ID") Long usuarioId) {
        try {
            Desafio desafioActualizado = desafioServicio.actualizarDesafio(id, desafioRequest, usuarioId);
            return ResponseEntity.ok(desafioActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar desafío: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDesafio(
            @PathVariable Long id,
            @RequestHeader("X-User-ID") Long usuarioId) {
        try {
            desafioServicio.eliminarDesafio(id, usuarioId);
            return ResponseEntity.ok("Desafío eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al eliminar desafío: " + e.getMessage());
        }
    }
}