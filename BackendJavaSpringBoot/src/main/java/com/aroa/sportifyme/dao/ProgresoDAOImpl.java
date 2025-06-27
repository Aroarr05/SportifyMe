package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ProgresoDAOImpl implements ProgresoDAO {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Progreso> progresoRowMapper;

    public ProgresoDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.progresoRowMapper = crearProgresoRowMapper();
    }

    @Override
    public Progreso registrarProgreso(Progreso progreso) {
        if (progreso.getFechaRegistro() == null) {
            progreso.setFechaRegistro(LocalDateTime.now());
        }

        String sql = """
        INSERT INTO progresos (valor_actual, fecha_registro, usuario_id, desafio_id, comentario)
        VALUES (?, ?, ?, ?, ?)
        """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, progreso.getValorActual());
            ps.setTimestamp(2, Timestamp.valueOf(progreso.getFechaRegistro()));
            ps.setLong(3, progreso.getUsuario().getId());
            ps.setLong(4, progreso.getDesafio().getId());
            ps.setString(5, progreso.getComentario());
            return ps;
        }, keyHolder);

        Optional.ofNullable(keyHolder.getKey())
                .ifPresent(key -> progreso.setId(key.longValue()));

        return progreso;
    }

    @Override
    public List<Progreso> obtenerProgresosPorDesafio(Long desafioId) {
        String sql = """
            SELECT p.id, p.valor_actual, p.fecha_registro, p.comentario,
                   u.id as usuario_id, u.nombre as usuario_nombre,
                   d.id as desafio_id, d.titulo as desafio_titulo
            FROM progresos p
            JOIN usuarios u ON p.usuario_id = u.id
            JOIN desafios d ON p.desafio_id = d.id
            WHERE p.desafio_id = ?
            ORDER BY p.fecha_registro DESC
            """;

        return jdbcTemplate.query(sql, progresoRowMapper, desafioId);
    }

    @Override
    public List<Progreso> obtenerRankingDesafio(Long desafioId) {
        String sql = """
            SELECT p.id, p.valor_actual, p.fecha_registro, p.comentario,
                   u.id as usuario_id, u.nombre as usuario_nombre,
                   d.id as desafio_id, d.titulo as desafio_titulo
            FROM progresos p
            JOIN usuarios u ON p.usuario_id = u.id
            JOIN desafios d ON p.desafio_id = d.id
            WHERE p.desafio_id = ?
            ORDER BY p.valor_actual DESC, p.fecha_registro ASC
            LIMIT 20
            """;

        return jdbcTemplate.query(sql, progresoRowMapper, desafioId);
    }

    @Override
    public Optional<Progreso> buscarPorId(Long id) {
        String sql = """
            SELECT p.id, p.valor_actual, p.fecha_registro, p.comentario,
                   u.id as usuario_id, u.nombre as usuario_nombre,
                   d.id as desafio_id, d.titulo as desafio_titulo
            FROM progresos p
            JOIN usuarios u ON p.usuario_id = u.id
            JOIN desafios d ON p.desafio_id = d.id
            WHERE p.id = ?
            """;

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, progresoRowMapper, id));
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Progreso> crearProgresoRowMapper() {
        return (rs, rowNum) -> {
            Progreso progreso = new Progreso();
            progreso.setId(rs.getLong("id"));
            progreso.setValorActual(rs.getDouble("valor_actual"));
            progreso.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
            progreso.setComentario(rs.getString("comentario"));

            // Construir objetos relacionados con la información mínima necesaria
            Usuario usuario = new Usuario();
            usuario.setId(rs.getLong("usuario_id"));
            usuario.setNombre(rs.getString("usuario_nombre"));
            progreso.setUsuario(usuario);

            Desafio desafio = new Desafio();
            desafio.setId(rs.getLong("desafio_id"));
            desafio.setTitulo(rs.getString("desafio_titulo"));
            progreso.setDesafio(desafio);

            return progreso;
        };
    }
}