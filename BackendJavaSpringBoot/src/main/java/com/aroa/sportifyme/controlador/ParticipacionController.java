package com.aroa.sportifyme.controlador;


import com.aroa.sportifyme.exception.*;
import com.aroa.sportifyme.modelo.Participacion;
import com.aroa.sportifyme.servicio.ParticipacionServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participaciones")
@RequiredArgsConstructor
@Tag(name = "Participaciones", description = "Gestión de participación en desafíos")
public class ParticipacionController {

    private final ParticipacionServicio participacionServicio;

    @PostMapping("/desafio/{desafioId}")
    @Operation(summary = "Unirse a un desafío")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Participación creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Desafío expirado o límite de participantes alcanzado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "409", description = "El usuario ya participa en este desafío")
    })
    public ResponseEntity<com.aroa.sportifyme.seguridad.dto.ApiResponse<Participacion>> unirseADesafio(
            @PathVariable Long desafioId,
            Authentication authentication) {
        try {
            Participacion participacion = participacionServicio.unirseADesafio(
                    desafioId,
                    authentication.getName()
            );
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(com.aroa.sportifyme.seguridad.dto.ApiResponse.success(
                            "Te has unido al desafío correctamente",
                            participacion
                    ));
        } catch (ParticipacionExistenteException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(com.aroa.sportifyme.seguridad.dto.ApiResponse.error(e.getMessage()));
        } catch (AccesoNoAutorizadoException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(com.aroa.sportifyme.seguridad.dto.ApiResponse.error(e.getMessage()));
        } catch (DesafioExpiradoException | LimiteParticipantesException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(com.aroa.sportifyme.seguridad.dto.ApiResponse.error(e.getMessage()));
        } catch (UsuarioNoEncontradoException | DesafioNoEncontradoException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(com.aroa.sportifyme.seguridad.dto.ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{participacionId}")
    @Operation(summary = "Abandonar un desafío")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Participación eliminada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Desafío ya expirado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido (no es el participante o admin)"),
            @ApiResponse(responseCode = "404", description = "Participación no encontrada")
    })
    public ResponseEntity<com.aroa.sportifyme.seguridad.dto.ApiResponse<Void>> abandonarDesafio(
            @PathVariable Long participacionId,
            Authentication authentication) {
        try {
            participacionServicio.abandonarDesafio(
                    participacionId,
                    authentication.getName()
            );
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(com.aroa.sportifyme.seguridad.dto.ApiResponse.success(
                            "Has abandonado el desafío correctamente",
                            null
                    ));
        } catch (AccesoNoAutorizadoException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(com.aroa.sportifyme.seguridad.dto.ApiResponse.error(e.getMessage()));
        } catch (DesafioExpiradoException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(com.aroa.sportifyme.seguridad.dto.ApiResponse.error(e.getMessage()));
        } catch (ParticipacionNoEncontradaException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(com.aroa.sportifyme.seguridad.dto.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener participaciones por usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de participaciones obtenida"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<com.aroa.sportifyme.seguridad.dto.ApiResponse<List<Participacion>>> obtenerParticipacionesPorUsuario(
            @PathVariable Long usuarioId) {
        List<Participacion> participaciones = participacionServicio.obtenerParticipacionesPorUsuario(usuarioId);
        return ResponseEntity.ok(
                com.aroa.sportifyme.seguridad.dto.ApiResponse.success(
                        "Participaciones obtenidas exitosamente",
                        participaciones
                )
        );
    }

    @GetMapping("/desafio/{desafioId}")
    @Operation(summary = "Obtener participantes por desafío")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de participantes obtenida"),
            @ApiResponse(responseCode = "404", description = "Desafío no encontrado")
    })
    public ResponseEntity<com.aroa.sportifyme.seguridad.dto.ApiResponse<List<Participacion>>> obtenerParticipantesPorDesafio(
            @PathVariable Long desafioId) {
        List<Participacion> participantes = participacionServicio.obtenerParticipantesPorDesafio(desafioId);
        return ResponseEntity.ok(
                com.aroa.sportifyme.seguridad.dto.ApiResponse.success(
                        "Participantes obtenidos exitosamente",
                        participantes
                )
        );
    }
}