package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.dao.DesafioDAO;
import com.aroa.sportifyme.modelo.Desafio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class DesafioServicio {

    private final DesafioDAO desafioDAO;

    public DesafioServicio(DesafioDAO desafioDAO) {
        this.desafioDAO = desafioDAO;
    }

    @Transactional
    public Desafio crearDesafio(Desafio desafio) {
        return desafioDAO.guardar(desafio);
    }

    public List<Desafio> listarDesafiosActivos() {
        return desafioDAO.listarDesafiosActivos();
    }

    public Optional<Desafio> buscarPorId(Long id) {
        return desafioDAO.buscarPorId(id);
    }

    @Transactional
    public void eliminarDesafio(Long id) {
        desafioDAO.eliminar(id);
    }

    public List<Desafio> listarTodos() {
        return desafioDAO.listarTodos();
    }
}