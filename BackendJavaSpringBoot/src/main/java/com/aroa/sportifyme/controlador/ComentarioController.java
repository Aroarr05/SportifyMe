package com.aroa.sportifyme.controlador;

import com.aroa.sportifyme.modelo.Comentario;
import com.aroa.sportifyme.servicio.ComentarioServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
@Tag(name = "Comentarios", description = "Gestión de comentarios en desafíos")
public class ComentarioController {

    private final ComentarioServicio comentarioServicio;

    @PostMapping("/desafio/{desafioId}")
    @Operation(summary = "Crear un nuevo comentario")
    public ResponseEntity<Comentario> crearComentario(
            @PathVariable Long desafioId,
            @RequestBody String contenido,
            Authentication authentication) {

        Comentario comentario = comentarioServicio.crearComentario(
                desafioId,
                contenido,
                authentication.getName());

        return ResponseEntity.ok(comentario);
    }

    @GetMapping("/desafio/{desafioId}")
    @Operation(summary = "Obtener comentarios de un desafío")
    public ResponseEntity<List<Comentario>> obtenerComentariosPorDesafio(
            @PathVariable Long desafioId) {

        return ResponseEntity.ok(
                comentarioServicio.obtenerComentariosPorDesafio(desafioId));
    }

    @DeleteMapping("/{comentarioId}")
    @Operation(summary = "Eliminar un comentario")
    public ResponseEntity<Void> eliminarComentario(
            @PathVariable Long comentarioId,
            Authentication authentication) {

        comentarioServicio.eliminarComentario(comentarioId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}