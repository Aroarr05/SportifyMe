package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.Participacion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class ParticipacionDAOImpl implements ParticipacionDAO {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Participacion> participacionRowMapper;

    public ParticipacionDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.participacionRowMapper = (rs, rowNum) -> {
            Participacion participacion = new Participacion();
            participacion.setId(rs.getLong("id"));
            // Los objetos Usuario y Desafio se cargarán parcialmente
            return participacion;
        };
    }

    @Override
    public Participacion guardar(Participacion participacion) {
        String sql = """
            INSERT INTO participaciones (usuario_id, desafio_id)
            VALUES (?, ?)
            """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, participacion.getUsuario().getId());
            ps.setLong(2, participacion.getDesafio().getId());
            return ps;
        }, keyHolder);

        participacion.setId(keyHolder.getKey().longValue());
        return participacion;
    }

    @Override
    public Optional<Participacion> buscarPorId(Long id) {
        String sql = """
            SELECT p.id, p.usuario_id, p.desafio_id
            FROM participaciones p
            WHERE p.id = ?
            """;

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, participacionRowMapper, id));
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existeParticipacion(Long usuarioId, Long desafioId) {
        String sql = """
            SELECT COUNT(*) > 0 
            FROM participaciones 
            WHERE usuario_id = ? AND desafio_id = ?
            """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, usuarioId, desafioId));
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM participaciones WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}