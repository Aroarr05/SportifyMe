package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    // Métodos que ya vienen incluidos en JpaRepository:
    // save(), findById(), findAll(), deleteById(), count(), etc.
}