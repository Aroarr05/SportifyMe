package com.aroa.sportifyme.controlador;

import com.aroa.sportifyme.modelo.Participacion;
import com.aroa.sportifyme.servicio.ParticipacionServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/participaciones")
@RequiredArgsConstructor
@Tag(name = "Participaciones", description = "Gestión de participación en desafíos")
public class ParticipacionController {

    private final ParticipacionServicio participacionServicio;

    @PostMapping("/desafio/{desafioId}")
    @Operation(summary = "Unirse a un desafío",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Unión exitosa"),
                    @ApiResponse(responseCode = "400", description = "Error en la solicitud"),
                    @ApiResponse(responseCode = "401", description = "No autorizado")
            })
    public ResponseEntity<?> unirseADesafio(
            @Parameter(description = "ID del desafío") @PathVariable Long desafioId,
            Authentication authentication) {

        try {
            String email = authentication.getName();
            Participacion participacion = participacionServicio.unirseADesafio(desafioId, email);
            return ResponseEntity.ok(participacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage(),
                    "timestamp", System.currentTimeMillis()
            ));
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener participaciones de un usuario")
    public ResponseEntity<List<Participacion>> obtenerParticipacionesUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {

        List<Participacion> participaciones = participacionServicio.obtenerParticipacionesPorUsuario(usuarioId);
        return ResponseEntity.ok(participaciones);
    }

    @GetMapping("/desafio/{desafioId}")
    @Operation(summary = "Obtener participantes de un desafío")
    public ResponseEntity<List<Participacion>> obtenerParticipantesDesafio(
            @Parameter(description = "ID del desafío") @PathVariable Long desafioId) {

        List<Participacion> participantes = participacionServicio.obtenerParticipantesPorDesafio(desafioId);
        return new ResponseEntity<>(participantes, HttpStatus.OK);
    }

    @GetMapping("/verificar")
    @Operation(summary = "Verificar participación")
    public ResponseEntity<Boolean> verificarParticipacion(
            @Parameter(description = "ID del usuario") @RequestParam Long usuarioId,
            @Parameter(description = "ID del desafío") @RequestParam Long desafioId) {

        boolean participa = participacionServicio.usuarioParticipaEnDesafio(usuarioId, desafioId);
        return ResponseEntity.ok(participa);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Abandonar desafío",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Desafío abandonado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Participación no encontrada"),
                    @ApiResponse(responseCode = "400", description = "No se puede abandonar el desafío"),
                    @ApiResponse(responseCode = "403", description = "No autorizado para esta acción")
            })
    public ResponseEntity<?> abandonarDesafio(
            @Parameter(description = "ID de la participación") @PathVariable Long id,
            Authentication authentication) {

        try {
            Participacion participacion = participacionServicio.findById(id);

            if (!participacion.getUsuario().getEmail().equals(authentication.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "error", "No tienes permiso para abandonar esta participación",
                        "timestamp", System.currentTimeMillis()
                ));
            }

            participacionServicio.abandonarDesafio(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage(),
                    "timestamp", System.currentTimeMillis()
            ));
        }
    }
}