package com.aroa.sportifyme.dao.impl;

import com.aroa.sportifyme.dao.UsuarioDAO;
import com.aroa.sportifyme.modelo.Usuario;
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

    public UsuarioDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Usuario save(Usuario usuario) {
        if (usuario.getId() == null) {
            return createAndReturn(usuario);
        } else {
            update(usuario);
            return usuario;
        }
    }

    private Usuario createAndReturn(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, email, contraseña, avatar_url, fecha_registro, rol) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getContraseña());
            ps.setString(4, usuario.getAvatarUrl());
            ps.setObject(5, usuario.getFechaRegistro() != null ?
                    usuario.getFechaRegistro() : LocalDateTime.now());
            ps.setString(6, usuario.getRol().name());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            usuario.setId(keyHolder.getKey().longValue());
        }
        return usuario;
    }

    @Override
    public void create(Usuario usuario) {
        save(usuario);
    }

    @Override
    public List<Usuario> getAll() {
        String sql = "SELECT id, nombre, email, contraseña, avatar_url, fecha_registro, rol FROM usuarios";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getLong("id"));
            usuario.setNombre(rs.getString("nombre"));
            usuario.setEmail(rs.getString("email"));
            usuario.setContraseña(rs.getString("contraseña"));
            usuario.setAvatarUrl(rs.getString("avatar_url"));
            usuario.setFechaRegistro(rs.getObject("fecha_registro", LocalDateTime.class));
            usuario.setRol(Usuario.RolUsuario.valueOf(rs.getString("rol")));
            return usuario;
        });
    }

    @Override
    public Optional<Usuario> find(int id) {
        String sql = "SELECT id, nombre, email, contraseña, avatar_url, fecha_registro, rol FROM usuarios WHERE id = ?";
        try {
            Usuario usuario = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Usuario u = new Usuario();
                u.setId(rs.getLong("id"));
                u.setNombre(rs.getString("nombre"));
                u.setEmail(rs.getString("email"));
                u.setContraseña(rs.getString("contraseña"));
                u.setAvatarUrl(rs.getString("avatar_url"));
                u.setFechaRegistro(rs.getObject("fecha_registro", LocalDateTime.class));
                u.setRol(Usuario.RolUsuario.valueOf(rs.getString("rol")));
                return u;
            }, id);
            return Optional.ofNullable(usuario);
        } catch (Exception e) {
            log.error("Error al buscar usuario por ID: {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        String sql = "SELECT id, nombre, email, contraseña, avatar_url, fecha_registro, rol FROM usuarios WHERE email = ?";
        try {
            Usuario usuario = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Usuario u = new Usuario();
                u.setId(rs.getLong("id"));
                u.setNombre(rs.getString("nombre"));
                u.setEmail(rs.getString("email"));
                u.setContraseña(rs.getString("contraseña"));
                u.setAvatarUrl(rs.getString("avatar_url"));
                u.setFechaRegistro(rs.getObject("fecha_registro", LocalDateTime.class));
                u.setRol(Usuario.RolUsuario.valueOf(rs.getString("rol")));
                return u;
            }, email);
            return Optional.ofNullable(usuario);
        } catch (Exception e) {
            log.error("Error al buscar usuario por email: {}", email, e);
            return Optional.empty();
        }
    }

    @Override
    public void update(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, contraseña = ?, avatar_url = ?, rol = ? WHERE id = ?";
        int rows = jdbcTemplate.update(sql,
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getContraseña(),
                usuario.getAvatarUrl(),
                usuario.getRol().name(),
                usuario.getId());
        log.info("Actualizadas {} filas", rows);
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        log.info("Eliminadas {} filas", rows);
    }
}