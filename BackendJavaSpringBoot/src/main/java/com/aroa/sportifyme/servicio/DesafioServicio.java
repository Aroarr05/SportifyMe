package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.modelo.Desafio;
import com.aroa.sportifyme.modelo.Usuario;
import com.aroa.sportifyme.dao.DesafioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DesafioServicio {

    private final DesafioRepository desafioRepository;
    private final UsuarioServicio usuarioServicio;

    @Transactional
    public Desafio crearDesafio(Desafio desafio, Long creadorId) {
        Usuario creador = usuarioServicio.buscarPorId(creadorId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        desafio.setCreador(creador);
        return desafioRepository.save(desafio);
    }

    @Transactional(readOnly = true)
    public Desafio buscarPorId(Long id) {
        return desafioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Desafío no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Desafio> listarPorCreador(Long creadorId) {
        Usuario creador = usuarioServicio.buscarPorId(creadorId);
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
        Desafio desafioExistente = buscarPorId(id);
        Usuario usuario = usuarioServicio.buscarPorId(usuarioId);

        if (!desafioExistente.getCreador().equals(usuario)) {
            throw new IllegalArgumentException("No tienes permiso para modificar este desafío");
        }

        desafioExistente.setTitulo(desafioActualizado.getTitulo());
        desafioExistente.setDescripcion(desafioActualizado.getDescripcion());
        desafioExistente.setTipoActividad(desafioActualizado.getTipoActividad());
        desafioExistente.setObjetivo(desafioActualizado.getObjetivo());
        desafioExistente.setFechaInicio(desafioActualizado.getFechaInicio());
        desafioExistente.setFechaFin(desafioActualizado.getFechaFin());
        desafioExistente.setEsPublico(desafioActualizado.isEsPublico());

        return desafioRepository.save(desafioExistente);
    }

    @Transactional
    public void eliminarDesafio(Long id, Long usuarioId) {
        Desafio desafio = buscarPorId(id);
        Usuario usuario = usuarioServicio.buscarPorId(usuarioId);

        if (!desafio.getCreador().equals(usuario)) {
            throw new IllegalArgumentException("No tienes permiso para eliminar este desafío");
        }

        desafioRepository.delete(desafio);
    }

    @Transactional(readOnly = true)
    public boolean existeDesafio(Long id) {
        return desafioRepository.existsById(id);
    }
}