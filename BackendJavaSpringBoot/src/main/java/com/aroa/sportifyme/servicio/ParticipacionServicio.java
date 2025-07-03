package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.modelo.Desafio;
import com.aroa.sportifyme.modelo.Participacion;
import com.aroa.sportifyme.modelo.Usuario;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipacionServicio {

    private final ParticipacionDAO participacionDAO;
    private final UsuarioServicio usuarioServicio;
    private final DesafioServicio desafioServicio;

    @Transactional
    public Participacion unirseADesafio(Long desafioId, String usuarioEmail) {
        Usuario usuario = usuarioServicio.buscarPorEmail(usuarioEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Desafio desafio = desafioServicio.buscarPorId(desafioId)
                .orElseThrow(() -> new IllegalArgumentException("Desafío no encontrado"));

        validarFechaDesafio(desafio.getFechaFin().toLocalDate());

        if (participacionDAO.existsByUsuarioIdAndDesafioId(usuario.getId(), desafioId)) {
            throw new IllegalArgumentException("Ya estás participando en este desafío");
        }

        Participacion participacion = new Participacion(usuario, desafio);
        return participacionDAO.save(participacion);
    }

    @Transactional
    public void abandonarDesafio(Long participacionId) {
        Participacion participacion = findById(participacionId);

        if (LocalDate.now().isAfter(participacion.getDesafio().getFechaFin().toLocalDate())) {
            throw new IllegalStateException("No se puede abandonar un desafío que ya ha finalizado");
        }

        participacionDAO.delete(participacionId);
    }

    public Participacion findById(Long id) {
        return participacionDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Participación no encontrada"));
    }

    public List<Participacion> obtenerParticipacionesPorUsuario(Long usuarioId) {
        return participacionDAO.findByUsuarioId(usuarioId);
    }

    public List<Participacion> obtenerParticipantesPorDesafio(Long desafioId) {
        return participacionDAO.findByDesafioId(desafioId);
    }

    public boolean usuarioParticipaEnDesafio(Long usuarioId, Long desafioId) {
        return participacionDAO.existsByUsuarioIdAndDesafioId(usuarioId, desafioId);
    }

    private void validarFechaDesafio(LocalDate fechaLimite) {
        if (LocalDate.now().isAfter(fechaLimite)) {
            throw new IllegalArgumentException("El desafío ya ha expirado");
        }
    }
}