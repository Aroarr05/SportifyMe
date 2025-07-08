package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.exception.*;
import com.aroa.sportifyme.modelo.*;
import com.aroa.sportifyme.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DesafioServicio {

    private final DesafioRepository desafioRepository;
    private final UsuarioServicio usuarioServicio;

    @Transactional
    public Desafio crearDesafio(Desafio desafio, Long creadorId) {
        validarDesafio(desafio);

        Usuario creador = usuarioServicio.buscarPorId(creadorId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(creadorId));
        desafio.setCreador(creador);

        if (desafio.getEsPublico() == null) {
            desafio.setEsPublico(true);
        }

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
    public Desafio actualizarDesafio(Long id, Desafio desafioActualizado, Long usuarioId) {
        validarDesafio(desafioActualizado);

        Desafio desafioExistente = buscarPorId(id);
        Usuario usuario = usuarioServicio.buscarPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));

        if (!tienePermisosParaModificar(desafioExistente, usuario)) {
            throw new AccesoNoAutorizadoException("actualizar", "desafío", id);
        }

        actualizarCamposPermitidos(desafioExistente, desafioActualizado);
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

    private void validarDesafio(Desafio desafio) {
        if (desafio.getTitulo() == null || desafio.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        if (desafio.getTipoActividad() == null) {
            throw new IllegalArgumentException("El tipo de actividad es obligatorio");
        }
        if (desafio.getFechaInicio() == null || desafio.getFechaFin() == null) {
            throw new IllegalArgumentException("Las fechas son obligatorias");
        }
        if (desafio.getFechaFin().isBefore(desafio.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la de inicio");
        }
        if (desafio.getMaxParticipantes() != null && desafio.getMaxParticipantes() <= 0) {
            throw new IllegalArgumentException("El máximo de participantes debe ser positivo");
        }
    }

    private void actualizarCamposPermitidos(Desafio existente, Desafio actualizado) {
        existente.setTitulo(actualizado.getTitulo());
        existente.setDescripcion(actualizado.getDescripcion());
        existente.setTipoActividad(actualizado.getTipoActividad());
        existente.setObjetivo(actualizado.getObjetivo());
        existente.setUnidadObjetivo(actualizado.getUnidadObjetivo());
        existente.setFechaInicio(actualizado.getFechaInicio());
        existente.setFechaFin(actualizado.getFechaFin());
        existente.setEsPublico(actualizado.getEsPublico());
        existente.setDificultad(actualizado.getDificultad());
        existente.setMaxParticipantes(actualizado.getMaxParticipantes());
        existente.setImagenUrl(actualizado.getImagenUrl());
    }

    private boolean tienePermisosParaModificar(Desafio desafio, Usuario usuario) {
        return desafio.getCreador().equals(usuario) ||
                usuario.getRol() == Usuario.RolUsuario.ADMIN;
    }
}