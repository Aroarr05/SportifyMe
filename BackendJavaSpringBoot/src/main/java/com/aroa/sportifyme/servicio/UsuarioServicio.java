package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.dao.UsuarioDAO;
import com.aroa.sportifyme.modelo.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioServicio {

    private final UsuarioDAO usuarioDAO;
    private final PasswordEncoder passwordEncoder;

    // Inyección por constructor (mejor práctica)
    public UsuarioServicio(UsuarioDAO usuarioDAO, PasswordEncoder passwordEncoder) {
        this.usuarioDAO = usuarioDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public void registrarUsuario(Usuario usuario) {
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        usuarioDAO.create(usuario);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioDAO.findByEmail(email);
    }
}