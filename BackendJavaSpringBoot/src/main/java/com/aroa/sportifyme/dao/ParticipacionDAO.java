package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.Participacion;
import java.util.Optional;

public interface ParticipacionDAO {
    Participacion guardar(Participacion participacion);
    Optional<Participacion> buscarPorId(Long id);
    boolean existeParticipacion(Long usuarioId, Long desafioId);
    void eliminar(Long id);
}