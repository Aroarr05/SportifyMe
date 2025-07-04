package com.aroa.sportifyme.controlador;

import com.aroa.sportifyme.dto.ProgresoDTO;
import com.aroa.sportifyme.dto.RankingDTO;
import com.aroa.sportifyme.modelo.Progreso;
import com.aroa.sportifyme.servicio.ProgresoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/progresos")
@RequiredArgsConstructor
@Tag(name = "Progresos", description = "Gestión de progresos en desafíos")
public class ProgresoController {

    private final ProgresoServicio progresoServicio;

    @PostMapping
    @Operation(summary = "Registrar progreso",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Progreso registrado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "401", description = "No autorizado")
            })
    public ResponseEntity<?> registrarProgreso(
            @Valid @RequestBody ProgresoDTO progresoDTO,
            Authentication authentication) {

        try {
            String email = authentication.getName();
            Progreso progreso = progresoServicio.registrarProgreso(progresoDTO, email);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(progreso.getId())
                    .toUri();

            return ResponseEntity.created(location).body(progreso);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage(),
                    "timestamp", System.currentTimeMillis()
            ));
        }
    }

    @GetMapping("/desafio/{desafioId}")
    @Operation(summary = "Obtener progresos por desafío")
    public ResponseEntity<List<Progreso>> obtenerProgresosPorDesafio(
            @Parameter(description = "ID del desafío") @PathVariable Long desafioId) {

        List<Progreso> progresos = progresoServicio.obtenerProgresosPorDesafio(desafioId);
        return ResponseEntity.ok(progresos);
    }

    @GetMapping("/ranking/{desafioId}")
    @Operation(summary = "Obtener ranking de un desafío")
    public ResponseEntity<List<RankingDTO>> obtenerRankingDesafio(
            @Parameter(description = "ID del desafío") @PathVariable Long desafioId) {

        List<RankingDTO> ranking = progresoServicio.generarRankingDesafio(desafioId);
        return ResponseEntity.ok(ranking);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar progreso por ID")
    public ResponseEntity<Progreso> buscarProgresoPorId(
            @Parameter(description = "ID del progreso") @PathVariable Long id) {

        return progresoServicio.buscarProgresoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}