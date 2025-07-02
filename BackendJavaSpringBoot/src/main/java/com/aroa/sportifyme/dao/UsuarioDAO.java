package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioDAO {
    Usuario save(Usuario usuario);
    void create(Usuario usuario);
    List<Usuario> getAll();
    Optional<Usuario> find(int id);
    void update(Usuario usuario);
    void delete(long id);
    Optional<Usuario> findByEmail(String email);
}
