package com.aroa.sportifyme.controlador;

import com.aroa.sportifyme.modelo.Desafio;
import com.aroa.sportifyme.modelo.Usuario;
import com.aroa.sportifyme.seguridad.dto.DesafioDTO;
import com.aroa.sportifyme.servicio.DesafioServicio;
import com.aroa.sportifyme.servicio.UsuarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/desafios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DesafioController {

    private final DesafioServicio desafioServicio;
    private final UsuarioServicio usuarioServicio;

    // ✅ CORREGIDO: Usar DTO en lugar de la entidad directamente
    @GetMapping
    public ResponseEntity<List<DesafioDTO>> listarTodosLosDesafios() {
        try {
            List<Desafio> desafios = desafioServicio.listarTodos();
            List<DesafioDTO> desafiosDTO = desafios.stream()
                    .map(DesafioDTO::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(desafiosDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ✅ CORREGIDO: También para obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDesafioPorId(@PathVariable Long id) {
        try {
            Desafio desafio = desafioServicio.buscarPorId(id);
            DesafioDTO desafioDTO = DesafioDTO.fromEntity(desafio);
            return ResponseEntity.ok(desafioDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Endpoint para obtener participantes de un desafío
    @GetMapping("/{desafioId}/participantes")
    public ResponseEntity<?> obtenerParticipantes(@PathVariable Long desafioId) {
        try {
            List<Usuario> participantes = desafioServicio.obtenerParticipantesDesafio(desafioId);
            return ResponseEntity.ok(participantes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Endpoint para unirse a un desafío
    @PostMapping("/{desafioId}/unirse")
    public ResponseEntity<?> unirseADesafio(
            @PathVariable Long desafioId, 
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Long usuarioId = obtenerUsuarioIdDesdeUserDetails(userDetails);
            desafioServicio.unirseADesafio(desafioId, usuarioId);
            return ResponseEntity.ok().body(Map.of("message", "Te has unido al desafío exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Método auxiliar para obtener el ID del usuario
    private Long obtenerUsuarioIdDesdeUserDetails(UserDetails userDetails) {
        String username = userDetails.getUsername();
        return usuarioServicio.buscarPorEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username))
                .getId();
    }
}