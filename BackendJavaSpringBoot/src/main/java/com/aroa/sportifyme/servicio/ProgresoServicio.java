package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.dao.ProgresoDAO;
import com.aroa.sportifyme.modelo.Progreso;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProgresoServicio {

    private final ProgresoDAO progresoDAO;

    public ProgresoServicio(ProgresoDAO progresoDAO) {
        this.progresoDAO = progresoDAO;
    }

    @Transactional
    public Progreso registrarProgreso(Progreso progreso) {
        return progresoDAO.registrarProgreso(progreso);
    }

    public List<Progreso> obtenerProgresosPorDesafio(Long desafioId) {
        return progresoDAO.obtenerProgresosPorDesafio(desafioId);
    }

    public List<Progreso> obtenerRankingDesafio(Long desafioId) {
        return progresoDAO.obtenerRankingDesafio(desafioId);
    }

    public Optional<Progreso> buscarProgresoPorId(Long id) {
        return progresoDAO.buscarPorId(id);
    }
}