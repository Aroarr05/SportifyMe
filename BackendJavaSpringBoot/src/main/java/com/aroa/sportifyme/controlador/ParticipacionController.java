package com.aroa.sportifyme.controlador;


import com.aroa.sportifyme.exception.AccesoNoAutorizadoException;
import com.aroa.sportifyme.exception.DesafioExpiradoException;
import com.aroa.sportifyme.exception.ParticipacionExistenteException;
import com.aroa.sportifyme.modelo.Participacion;
import com.aroa.sportifyme.servicio.ParticipacionServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
                    @ApiResponse(responseCode = "401", description = "No autorizado"),
                    @ApiResponse(responseCode = "403", description = "Prohibido (ej. ya está participando)")
            })
    public ResponseEntity<ApiResponse> unirseADesafio(
            @PathVariable Long desafioId,
            Authentication authentication) {

        try {
            Participacion participacion = participacionServicio.unirseADesafio(
                    desafioId,
                    authentication.getName()
            );

            // Versión recomendada usando el método estático
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success(
                            "Te has unido al desafío correctamente",
                            participacion
                    ));

        } catch (RuntimeException e) {
            // Versión con manejo específico de códigos de estado
            HttpStatus status = determinarCodigoEstado(e);

            return ResponseEntity
                    .status(status)
                    .body(ApiResponse.error(
                            e.getMessage()
                    ));
        }
    }

    private HttpStatus determinarCodigoEstado(RuntimeException e) {
        // Personaliza según tus tipos de excepción
        if (e instanceof ParticipacionExistenteException) {
            return HttpStatus.CONFLICT; // 409
        } else if (e instanceof AccesoNoAutorizadoException) {
            return HttpStatus.FORBIDDEN; // 403
        } else if (e instanceof DesafioExpiradoException) {
            return HttpStatus.BAD_REQUEST; // 400
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR; // 500
        }
    }
}