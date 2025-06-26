package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.dao.UsuarioDAO;
import com.aroa.sportifyme.modelo.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void registrarUsuario(Usuario usuario) {
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        usuarioDAO.create(usuario);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioDAO.findByEmail(email);
    }
}