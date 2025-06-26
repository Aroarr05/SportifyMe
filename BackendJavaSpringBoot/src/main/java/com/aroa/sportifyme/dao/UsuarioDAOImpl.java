package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class UsuarioDAOImpl implements UsuarioDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsuarioDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Usuario usuario) {
        String sqlInsert = """
            INSERT INTO usuarios (nombre, email, contraseña, avatar_url, fecha_registro) 
            VALUES (?, ?, ?, ?, ?)
        """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getContraseña());
            ps.setString(4, usuario.getAvatarUrl());
            ps.setObject(5, usuario.getFechaRegistro());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            usuario.setId(keyHolder.getKey().longValue());
        }
        log.info("Insertados {} registros.", rows);
    }

    @Override
    public List<Usuario> getAll() {
        return jdbcTemplate.query(
                "SELECT id, nombre, email, contraseña, avatar_url, fecha_registro FROM usuarios",
                (rs, rowNum) -> {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getLong("id"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setContraseña(rs.getString("contraseña"));
                    usuario.setAvatarUrl(rs.getString("avatar_url"));
                    usuario.setFechaRegistro(rs.getObject("fecha_registro", LocalDateTime.class));
                    return usuario;
                }
        );
    }

    @Override
    public Optional<Usuario> find(int id) {
        try {
            Usuario usuario = jdbcTemplate.queryForObject(
                    "SELECT id, nombre, email, contraseña, avatar_url, fecha_registro FROM usuarios WHERE id = ?",
                    (rs, rowNum) -> {
                        Usuario u = new Usuario();
                        u.setId(rs.getLong("id"));
                        u.setNombre(rs.getString("nombre"));
                        u.setEmail(rs.getString("email"));
                        u.setContraseña(rs.getString("contraseña"));
                        u.setAvatarUrl(rs.getString("avatar_url"));
                        u.setFechaRegistro(rs.getObject("fecha_registro", LocalDateTime.class));
                        return u;
                    },
                    id
            );
            return Optional.ofNullable(usuario);
        } catch (Exception e) {
            log.error("Usuario no encontrado con ID: " + id, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        try {
            Usuario usuario = jdbcTemplate.queryForObject(
                    "SELECT id, nombre, email, contraseña, avatar_url, fecha_registro FROM usuarios WHERE email = ?",
                    (rs, rowNum) -> {
                        Usuario u = new Usuario();
                        u.setId(rs.getLong("id"));
                        u.setNombre(rs.getString("nombre"));
                        u.setEmail(rs.getString("email"));
                        u.setContraseña(rs.getString("contraseña"));
                        u.setAvatarUrl(rs.getString("avatar_url"));
                        u.setFechaRegistro(rs.getObject("fecha_registro", LocalDateTime.class));
                        return u;
                    },
                    email
            );
            return Optional.ofNullable(usuario);
        } catch (Exception e) {
            log.error("Usuario no encontrado con email: " + email, e);
            return Optional.empty();
        }
    }

    @Override
    public void update(Usuario usuario) {
        int rows = jdbcTemplate.update(
                """
                UPDATE usuarios SET 
                    nombre = ?, 
                    email = ?,
                    contraseña = ?,
                    avatar_url = ?
                WHERE id = ?
                """,
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getContraseña(),
                usuario.getAvatarUrl(),
                usuario.getId()
        );
        log.info("Actualizados {} registros.", rows);
    }

    @Override
    public void delete(long id) {
        int rows = jdbcTemplate.update("DELETE FROM usuarios WHERE id = ?", id);
        log.info("Eliminados {} registros.", rows);
    }
}