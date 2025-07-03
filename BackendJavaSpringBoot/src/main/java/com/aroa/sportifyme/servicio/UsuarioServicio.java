package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.modelo.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServicio implements UserDetailsService {

    private final UsuarioDAO usuarioDAO;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioDAO.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getContraseña(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }

    public boolean existePorEmail(String email) {
        return usuarioDAO.findByEmail(email).isPresent();
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if (existePorEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        return usuarioDAO.save(usuario);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioDAO.findByEmail(email);
    }
}