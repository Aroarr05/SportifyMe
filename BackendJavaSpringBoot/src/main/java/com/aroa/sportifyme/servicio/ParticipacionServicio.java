package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.dao.ParticipacionDAO;
import com.aroa.sportifyme.modelo.Participacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParticipacionServicio {

    private final ParticipacionDAO participacionDAO;

    public ParticipacionServicio(ParticipacionDAO participacionDAO) {
        this.participacionDAO = participacionDAO;
    }

    @Transactional
    public Participacion unirseADesafio(Participacion participacion) {
        return participacionDAO.guardar(participacion);
    }

    public boolean usuarioParticipaEnDesafio(Long usuarioId, Long desafioId) {
        return participacionDAO.existeParticipacion(usuarioId, desafioId);
    }
}