package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.Desafio;
import com.aroa.sportifyme.modelo.TipoActividad;
import com.aroa.sportifyme.modelo.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class DesafioDAOImpl implements DesafioDAO {

    private final JdbcTemplate jdbcTemplate;

    public DesafioDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Desafio guardar(Desafio desafio) {
        String sql = """
            INSERT INTO desafios (titulo, descripcion, tipo_actividad, objetivo, 
            fecha_inicio, fecha_fin, creador_id, es_publico)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, desafio.getTitulo());
            ps.setString(2, desafio.getDescripcion());
            ps.setString(3, desafio.getTipoActividad().name());
            ps.setDouble(4, desafio.getObjetivo());
            ps.setTimestamp(5, Timestamp.valueOf(desafio.getFechaInicio()));
            ps.setTimestamp(6, Timestamp.valueOf(desafio.getFechaFin()));
            ps.setLong(7, desafio.getCreador().getId());
            ps.setBoolean(8, desafio.isEsPublico());
            return ps;
        }, keyHolder);

        keyHolder.getKeyList().stream()
                .findFirst()
                .ifPresent(key -> desafio.setId((Long) key.get("id")));

        return desafio;
    }

    @Override
    public Optional<Desafio> buscarPorId(Long id) {
        String sql = """
            SELECT d.*, u.id as usuario_id, u.nombre, u.email 
            FROM desafios d
            JOIN usuarios u ON d.creador_id = u.id
            WHERE d.id = ?
            """;

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Desafio desafio = new Desafio();
                desafio.setId(rs.getLong("id"));
                desafio.setTitulo(rs.getString("titulo"));
                desafio.setDescripcion(rs.getString("descripcion"));
                desafio.setTipoActividad(TipoActividad.valueOf(rs.getString("tipo_actividad")));
                desafio.setObjetivo(rs.getDouble("objetivo"));
                desafio.setFechaInicio(rs.getTimestamp("fecha_inicio").toLocalDateTime());
                desafio.setFechaFin(rs.getTimestamp("fecha_fin").toLocalDateTime());
                desafio.setEsPublico(rs.getBoolean("es_publico"));

                Usuario creador = new Usuario();
                creador.setId(rs.getLong("usuario_id"));
                creador.setNombre(rs.getString("nombre"));
                creador.setEmail(rs.getString("email"));
                desafio.setCreador(creador);

                return desafio;
            }, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Desafio> listarPorCreador(Usuario creador) {
        String sql = """
            SELECT d.* FROM desafios d
            WHERE d.creador_id = ?
            ORDER BY d.fecha_inicio DESC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Desafio desafio = new Desafio();
            desafio.setId(rs.getLong("id"));
            desafio.setTitulo(rs.getString("titulo"));
            desafio.setDescripcion(rs.getString("descripcion"));
            desafio.setTipoActividad(TipoActividad.valueOf(rs.getString("tipo_actividad")));
            desafio.setObjetivo(rs.getDouble("objetivo"));
            desafio.setFechaInicio(rs.getTimestamp("fecha_inicio").toLocalDateTime());
            desafio.setFechaFin(rs.getTimestamp("fecha_fin").toLocalDateTime());
            desafio.setEsPublico(rs.getBoolean("es_publico"));
            desafio.setCreador(creador);
            return desafio;
        }, creador.getId());
    }

    @Override
    public List<Desafio> listarDesafiosActivos() {
        String sql = """
            SELECT d.*, u.id as usuario_id, u.nombre 
            FROM desafios d
            JOIN usuarios u ON d.creador_id = u.id
            WHERE d.fecha_fin > CURRENT_TIMESTAMP
            AND d.es_publico = true
            ORDER BY d.fecha_inicio
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Desafio desafio = new Desafio();
            desafio.setId(rs.getLong("id"));
            desafio.setTitulo(rs.getString("titulo"));
            desafio.setDescripcion(rs.getString("descripcion"));
            desafio.setTipoActividad(TipoActividad.valueOf(rs.getString("tipo_actividad")));
            desafio.setObjetivo(rs.getDouble("objetivo"));
            desafio.setFechaInicio(rs.getTimestamp("fecha_inicio").toLocalDateTime());
            desafio.setFechaFin(rs.getTimestamp("fecha_fin").toLocalDateTime());
            desafio.setEsPublico(rs.getBoolean("es_publico"));

            Usuario creador = new Usuario();
            creador.setId(rs.getLong("usuario_id"));
            creador.setNombre(rs.getString("nombre"));
            desafio.setCreador(creador);

            return desafio;
        });
    }

    // Nuevo método para listar todos los desafíos
    public List<Desafio> listarTodos() {
        String sql = """
            SELECT d.*, u.id as usuario_id, u.nombre 
            FROM desafios d
            JOIN usuarios u ON d.creador_id = u.id
            ORDER BY d.fecha_inicio DESC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Desafio desafio = new Desafio();
            desafio.setId(rs.getLong("id"));
            desafio.setTitulo(rs.getString("titulo"));
            desafio.setDescripcion(rs.getString("descripcion"));
            desafio.setTipoActividad(TipoActividad.valueOf(rs.getString("tipo_actividad")));
            desafio.setObjetivo(rs.getDouble("objetivo"));
            desafio.setFechaInicio(rs.getTimestamp("fecha_inicio").toLocalDateTime());
            desafio.setFechaFin(rs.getTimestamp("fecha_fin").toLocalDateTime());
            desafio.setEsPublico(rs.getBoolean("es_publico"));

            Usuario creador = new Usuario();
            creador.setId(rs.getLong("usuario_id"));
            creador.setNombre(rs.getString("nombre"));
            desafio.setCreador(creador);

            return desafio;
        });
    }

    @Override
    public void actualizar(Desafio desafio) {
        String sql = """
            UPDATE desafios SET
                titulo = ?,
                descripcion = ?,
                tipo_actividad = ?,
                objetivo = ?,
                fecha_inicio = ?,
                fecha_fin = ?,
                es_publico = ?
            WHERE id = ?
            """;

        jdbcTemplate.update(sql,
                desafio.getTitulo(),
                desafio.getDescripcion(),
                desafio.getTipoActividad().name(),
                desafio.getObjetivo(),
                Timestamp.valueOf(desafio.getFechaInicio()),
                Timestamp.valueOf(desafio.getFechaFin()),
                desafio.isEsPublico(),
                desafio.getId());
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM desafios WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}