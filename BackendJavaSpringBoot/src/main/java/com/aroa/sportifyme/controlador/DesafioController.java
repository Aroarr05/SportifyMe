package com.aroa.sportifyme.controlador;

import com.aroa.sportifyme.modelo.Usuario;
import com.aroa.sportifyme.servicio.DesafioServicio;
import com.aroa.sportifyme.servicio.UsuarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/desafios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DesafioController {

    private final DesafioServicio desafioServicio;
    private final UsuarioServicio usuarioServicio;

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

    // Endpoint para obtener un desafío por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDesafioPorId(@PathVariable Long id) {
        try {
            Object desafio = desafioServicio.buscarPorId(id);
            return ResponseEntity.ok(desafio);
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