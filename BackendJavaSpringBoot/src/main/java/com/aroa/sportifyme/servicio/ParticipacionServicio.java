package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.exception.*;
import com.aroa.sportifyme.modelo.*;
import com.aroa.sportifyme.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipacionServicio {

    private final ParticipacionRepository participacionRepository;
    private final UsuarioServicio usuarioServicio;
    private final DesafioServicio desafioServicio;

    @Transactional
    public Participacion unirseADesafio(Long desafioId, String emailUsuario) {
        Usuario usuario = usuarioServicio.buscarPorEmail(emailUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException(emailUsuario));

        Desafio desafio = desafioServicio.buscarPorId(desafioId);

        validarParticipacion(usuario, desafio);

        Participacion participacion = new Participacion();
        participacion.setUsuario(usuario);
        participacion.setDesafio(desafio);
        participacion.setFechaUnion(LocalDateTime.now());

        return participacionRepository.save(participacion);
    }

    private void validarParticipacion(Usuario usuario, Desafio desafio) {
        // Verificar si ya está participando
        if (participacionRepository.existsByUsuarioIdAndDesafioId(usuario.getId(), desafio.getId())) {
            throw new ParticipacionExistenteException(usuario.getId(), desafio.getId());
        }

        // Verificar fechas del desafío
        if (LocalDateTime.now().isAfter(desafio.getFechaFin())) {
            throw new DesafioExpiradoException(desafio.getId());
        }

        // Verificar límite de participantes
        if (desafio.getMaxParticipantes() != null &&
                participacionRepository.countByDesafioId(desafio.getId()) >= desafio.getMaxParticipantes()) {
            throw new LimiteParticipantesException(desafio.getId(), desafio.getMaxParticipantes());
        }
    }

    @Transactional
    public void abandonarDesafio(Long participacionId, String emailUsuario) {
        Participacion participacion = findById(participacionId);
        Usuario usuario = usuarioServicio.buscarPorEmail(emailUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException(emailUsuario));

        validarPermisosAbandono(participacion, usuario);
        validarFechaDesafio(participacion.getDesafio());

        participacionRepository.delete(participacion);
    }

    private void validarPermisosAbandono(Participacion participacion, Usuario usuario) {
        if (!participacion.getUsuario().equals(usuario) &&
                !usuario.getRol().equals(Usuario.RolUsuario.ADMIN)) {
            throw new AccesoNoAutorizadoException("abandonar", "participación", participacion.getId());
        }
    }

    private void validarFechaDesafio(Desafio desafio) {
        if (LocalDateTime.now().isAfter(desafio.getFechaFin())) {
            throw new DesafioExpiradoException(desafio.getId());
        }
    }

    @Transactional(readOnly = true)
    public Participacion findById(Long id) {
        return participacionRepository.findById(id)
                .orElseThrow(() -> new ParticipacionNoEncontradaException(id));
    }

    @Transactional(readOnly = true)
    public List<Participacion> obtenerParticipacionesPorUsuario(Long usuarioId) {
        return participacionRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Participacion> obtenerParticipantesPorDesafio(Long desafioId) {
        return participacionRepository.findByDesafioId(desafioId);
    }

    @Transactional(readOnly = true)
    public boolean usuarioParticipaEnDesafio(Long usuarioId, Long desafioId) {
        return participacionRepository.existsByUsuarioIdAndDesafioId(usuarioId, desafioId);
    }
}