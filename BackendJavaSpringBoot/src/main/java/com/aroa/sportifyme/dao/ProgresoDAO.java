package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.Progreso;
import java.util.List;
import java.util.Optional;

public interface ProgresoDAO {
    Progreso registrarProgreso(Progreso progreso);
    List<Progreso> obtenerProgresosPorDesafio(Long desafioId);
    List<Progreso> obtenerRankingDesafio(Long desafioId);
    Optional<Progreso> buscarPorId(Long id);
}