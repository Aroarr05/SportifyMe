package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.Progreso;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ProgresoDAOImpl implements ProgresoDAO {

    private final JdbcTemplate jdbcTemplate;

    public ProgresoDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void registrarProgreso(Progreso progreso) {
        String sql = """
            INSERT INTO progresos (valor_actual, fecha_registro, usuario_id, desafio_id)
            VALUES (?, ?, ?, ?)
            """;

        jdbcTemplate.update(sql,
                progreso.getValorActual(),
                java.sql.Timestamp.valueOf(progreso.getFechaRegistro()),
                progreso.getUsuario().getId(),
                progreso.getDesafio().getId()
        );
    }

    @Override
    public List<Progreso> obtenerRankingDesafio(Long desafioId) {
        String sql = """
            SELECT p.*, u.nombre as usuario_nombre 
            FROM progresos p
            JOIN usuarios u ON p.usuario_id = u.id
            WHERE p.desafio_id = ?
            ORDER BY p.valor_actual DESC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Progreso progreso = new Progreso();
            progreso.setId(rs.getLong("id"));
            progreso.setValorActual(rs.getDouble("valor_actual"));
            progreso.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
            // Setear usuario y desafio (simplificado)
            return progreso;
        }, desafioId);
    }
}