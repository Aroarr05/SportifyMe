package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.Desafio;
import com.aroa.sportifyme.modelo.Participacion;
import com.aroa.sportifyme.modelo.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParticipacionDAOImpl implements ParticipacionDAO {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Participacion save(Participacion participacion) {
        if (participacion.getId() == null) {
            return insert(participacion);
        } else {
            throw new UnsupportedOperationException("Actualización no soportada");
        }
    }

    private Participacion insert(Participacion participacion) {
        String sql = "INSERT INTO participaciones (usuario_id, desafio_id, fecha_union) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, participacion.getUsuario().getId());
            ps.setLong(2, participacion.getDesafio().getId());
            ps.setTimestamp(3, Timestamp.valueOf(participacion.getFechaUnion()));
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            participacion.setId(keyHolder.getKey().longValue());
        }
        return participacion;
    }

    @Override
    public boolean existsByUsuarioIdAndDesafioId(Long usuarioId, Long desafioId) {
        String sql = "SELECT COUNT(*) FROM participaciones WHERE usuario_id = ? AND desafio_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, usuarioId, desafioId);
        return count != null && count > 0;
    }

    @Override
    public List<Participacion> findByUsuarioId(Long usuarioId) {
        String sql = "SELECT * FROM participaciones WHERE usuario_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToParticipacion(rs), usuarioId);
    }

    @Override
    public List<Participacion> findByDesafioId(Long desafioId) {
        String sql = "SELECT * FROM participaciones WHERE desafio_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToParticipacion(rs), desafioId);
    }

    @Override
    public Optional<Participacion> findById(Long id) {
        String sql = "SELECT * FROM participaciones WHERE id = ?";
        try {
            Participacion participacion = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> mapRowToParticipacion(rs), id);
            return Optional.ofNullable(participacion);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM participaciones WHERE id = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        if (affectedRows == 0) {
            throw new IllegalArgumentException("No se encontró la participación con ID: " + id);
        }
    }

    private Participacion mapRowToParticipacion(java.sql.ResultSet rs) throws java.sql.SQLException {
        Participacion participacion = new Participacion();
        participacion.setId(rs.getLong("id"));

        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("usuario_id"));
        participacion.setUsuario(usuario);

        Desafio desafio = new Desafio();
        desafio.setId(rs.getLong("desafio_id"));
        participacion.setDesafio(desafio);

        participacion.setFechaUnion(rs.getTimestamp("fecha_union").toLocalDateTime());
        return participacion;
    }
}