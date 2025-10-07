package com.aroa.sportifyme.controlador;

import com.aroa.sportifyme.modelo.Usuario;
import com.aroa.sportifyme.seguridad.dto.LoginDTO;
import com.aroa.sportifyme.seguridad.dto.RegistroDTO;
import com.aroa.sportifyme.seguridad.jwt.JwtAuthResponse;
import com.aroa.sportifyme.seguridad.jwt.JwtTokenProvider;
import com.aroa.sportifyme.servicio.UsuarioServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioServicio usuarioServicio;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generarToken(authentication);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwt)
                .body(new JwtAuthResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistroDTO registroDTO) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombre(registroDTO.getNombre());
            usuario.setEmail(registroDTO.getEmail());
            usuario.setContraseña(registroDTO.getPassword());
            usuario.setRol(Usuario.RolUsuario.usuario); 

            Usuario usuarioRegistrado = usuarioServicio.registrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRegistrado);

        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
}