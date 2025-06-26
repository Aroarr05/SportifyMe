package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.Desafio;
import com.aroa.sportifyme.modelo.TipoActividad;
import com.aroa.sportifyme.modelo.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class DesafioDAOImpl implements DesafioDAO {

    private final JdbcTemplate jdbcTemplate;

    public DesafioDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void crear(Desafio desafio) {
        String sql = """
            INSERT INTO desafios (titulo, descripcion, tipo_actividad, objetivo, 
            fecha_inicio, fecha_fin, creador_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, desafio.getTitulo());
            ps.setString(2, desafio.getDescripcion());
            ps.setString(3, desafio.getTipoActividad().name());
            ps.setDouble(4, desafio.getObjetivo());
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(desafio.getFechaInicio()));
            ps.setTimestamp(6, java.sql.Timestamp.valueOf(desafio.getFechaFin()));
            ps.setLong(7, desafio.getCreador().getId());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            desafio.setId(keyHolder.getKey().longValue());
        }
    }

    @Override
    public Desafio encontrarPorId(Long id) {
        String sql = "SELECT * FROM desafios WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Desafio desafio = new Desafio();
            desafio.setId(rs.getLong("id"));
            desafio.setTitulo(rs.getString("titulo"));
            desafio.setDescripcion(rs.getString("descripcion"));
            desafio.setTipoActividad(TipoActividad.valueOf(rs.getString("tipo_actividad")));
            desafio.setObjetivo(rs.getDouble("objetivo"));
            desafio.setFechaInicio(rs.getTimestamp("fecha_inicio").toLocalDateTime());
            desafio.setFechaFin(rs.getTimestamp("fecha_fin").toLocalDateTime());

            // Cargar creador (puede requerir una consulta adicional o JOIN)
            return desafio;
        }, id);
    }

    // Implementar otros métodos...
}