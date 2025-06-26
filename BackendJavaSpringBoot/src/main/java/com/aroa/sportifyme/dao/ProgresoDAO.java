package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.Progreso;
import java.util.List;

public interface ProgresoDAO {
    void registrarProgreso(Progreso progreso);
    List<Progreso> obtenerProgresosPorDesafio(Long desafioId);
    List<Progreso> obtenerRankingDesafio(Long desafioId);
}