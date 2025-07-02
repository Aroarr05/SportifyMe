package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.Participacion;
import java.util.List;
import java.util.Optional;

public interface ParticipacionDAO {
    Participacion save(Participacion participacion);
    boolean existsByUsuarioIdAndDesafioId(Long usuarioId, Long desafioId);
    List<Participacion> findByUsuarioId(Long usuarioId);
    List<Participacion> findByDesafioId(Long desafioId);
    Optional<Participacion> findById(Long id);
    void delete(Long id);
}