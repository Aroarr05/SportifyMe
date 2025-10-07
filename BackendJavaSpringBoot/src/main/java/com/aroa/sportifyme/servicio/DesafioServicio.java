package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.exception.*;
import com.aroa.sportifyme.modelo.*;
import com.aroa.sportifyme.repository.*;
import com.aroa.sportifyme.seguridad.dto.request.DesafioRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DesafioServicio {

    private final DesafioRepository desafioRepository;
    private final UsuarioServicio usuarioServicio;

    @Transactional
    public Desafio crearDesafio(DesafioRequest desafioRequest, Long creadorId) {
        Usuario creador = usuarioServicio.buscarPorId(creadorId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(creadorId));

        validarFechas(desafioRequest.getFechaInicio(), desafioRequest.getFechaFin());

        Desafio desafio = new Desafio();
        desafio.setTitulo(desafioRequest.getTitulo());
        desafio.setDescripcion(desafioRequest.getDescripcion());
        desafio.setTipoActividad(desafioRequest.getTipoActividad());
        if (desafioRequest.getObjetivo() != null) {
            desafio.setObjetivo(BigDecimal.valueOf(desafioRequest.getObjetivo()));
        }
        desafio.setUnidadObjetivo(desafioRequest.getUnidadObjetivo());
        desafio.setFechaInicio(desafioRequest.getFechaInicio());
        desafio.setFechaFin(desafioRequest.getFechaFin());
        desafio.setEsPublico(desafioRequest.getEsPublico());
        desafio.setDificultad(desafioRequest.getDificultad());
        desafio.setMaxParticipantes(desafioRequest.getMaxParticipantes());
        desafio.setCreador(creador);

        return desafioRepository.save(desafio);
    }

    @Transactional(readOnly = true)
    public Desafio buscarPorId(Long id) {
        return desafioRepository.findById(id)
                .orElseThrow(() -> new DesafioNoEncontradoException(id));
    }

    @Transactional(readOnly = true)
    public Optional<Desafio> buscarPorIdOptional(Long id) {
        return desafioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Desafio> listarPorCreador(Long creadorId) {
        Usuario creador = usuarioServicio.buscarPorId(creadorId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(creadorId));
        return desafioRepository.findByCreadorOrderByFechaInicioDesc(creador);
    }

    @Transactional(readOnly = true)
    public List<Desafio> listarDesafiosActivos() {
        return desafioRepository.findByEsPublicoTrueAndFechaFinAfterOrderByFechaInicioAsc(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<Desafio> listarTodos() {
        return desafioRepository.findAllByOrderByFechaInicioDesc();
    }

    @Transactional
    public Desafio actualizarDesafio(Long id, DesafioRequest desafioRequest, Long usuarioId) {
        Desafio desafioExistente = buscarPorId(id);
        Usuario usuario = usuarioServicio.buscarPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));

        validarFechas(desafioRequest.getFechaInicio(), desafioRequest.getFechaFin());

        if (!tienePermisosParaModificar(desafioExistente, usuario)) {
            throw new AccesoNoAutorizadoException("actualizar", "desafío", id);
        }

        actualizarCamposPermitidos(desafioExistente, desafioRequest);
        return desafioRepository.save(desafioExistente);
    }

    @Transactional
    public void eliminarDesafio(Long id, Long usuarioId) {
        Desafio desafio = buscarPorId(id);
        Usuario usuario = usuarioServicio.buscarPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));

        if (!tienePermisosParaModificar(desafio, usuario)) {
            throw new AccesoNoAutorizadoException("eliminar", "desafío", id);
        }

        desafioRepository.delete(desafio);
    }

    @Transactional(readOnly = true)
    public boolean existeDesafio(Long id) {
        return desafioRepository.existsById(id);
    }

    private void validarFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la de inicio");
        }
    }

    private void actualizarCamposPermitidos(Desafio existente, DesafioRequest request) {
        existente.setTitulo(request.getTitulo());
        existente.setDescripcion(request.getDescripcion());
        existente.setTipoActividad(request.getTipoActividad());
        if (request.getObjetivo() != null) {
            existente.setObjetivo(BigDecimal.valueOf(request.getObjetivo()));
        } else {
            existente.setObjetivo(null);
        }
        existente.setUnidadObjetivo(request.getUnidadObjetivo());
        existente.setFechaInicio(request.getFechaInicio());
        existente.setFechaFin(request.getFechaFin());
        existente.setEsPublico(request.getEsPublico());
        existente.setDificultad(request.getDificultad());
        existente.setMaxParticipantes(request.getMaxParticipantes());
        // Nota: La imagenUrl no está en el DTO, se manejaría por separado.
    }

    private boolean tienePermisosParaModificar(Desafio desafio, Usuario usuario) {
        return desafio.getCreador().equals(usuario) ||
                usuario.getRol() == Usuario.RolUsuario.admin;
    }
}