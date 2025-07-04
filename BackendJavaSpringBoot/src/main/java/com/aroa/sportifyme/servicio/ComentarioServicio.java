package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.exception.*;
import com.aroa.sportifyme.modelo.*;
import com.aroa.sportifyme.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ComentarioServicio {

    private final ComentarioRepository comentarioRepository;
    private final UsuarioServicio usuarioServicio;
    private final DesafioServicio desafioServicio;

    @Transactional
    public Comentario crearComentario(Long desafioId, String contenido, String emailUsuario) {
        validarContenido(contenido);

        Usuario usuario = usuarioServicio.buscarPorEmail(emailUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException(emailUsuario));

        Desafio desafio = desafioServicio.buscarPorId(desafioId);
        validarParticipacionUsuario(usuario, desafio);

        Comentario comentario = new Comentario();
        comentario.setContenido(contenido);
        comentario.setUsuario(usuario);
        comentario.setDesafio(desafio);
        comentario.setFechaCreacion(LocalDateTime.now());

        return comentarioRepository.save(comentario);
    }

    @Transactional
    public Comentario editarComentario(Long comentarioId, String nuevoContenido, String emailUsuario) {
        validarContenido(nuevoContenido);

        Comentario comentario = buscarComentario(comentarioId);
        Usuario usuario = usuarioServicio.buscarPorEmail(emailUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException(emailUsuario));

        validarPermisosEdicion(comentario, usuario);

        comentario.setContenido(nuevoContenido);
        comentario.setEditado(true);
        comentario.setFechaEdicion(LocalDateTime.now());

        return comentarioRepository.save(comentario);
    }

    @Transactional
    public void eliminarComentario(Long comentarioId, String emailUsuario) {
        Comentario comentario = buscarComentario(comentarioId);
        Usuario usuario = usuarioServicio.buscarPorEmail(emailUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException(emailUsuario));

        validarPermisosEliminacion(comentario, usuario);
        comentarioRepository.delete(comentario);
    }

    @Transactional(readOnly = true)
    public List<Comentario> obtenerComentariosPorDesafio(Long desafioId) {
        if (!desafioServicio.existeDesafio(desafioId)) {
            throw new DesafioNoEncontradoException(desafioId);
        }
        return comentarioRepository.findByDesafioIdOrderByFechaCreacionDesc(desafioId);
    }

    @Transactional(readOnly = true)
    public Comentario buscarComentario(Long id) {
        return comentarioRepository.findById(id)
                .orElseThrow(() -> new ComentarioNoEncontradoException(id));
    }

    private void validarContenido(String contenido) {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido no puede estar vacío");
        }
        if (contenido.length() > 500) {
            throw new IllegalArgumentException("El comentario no puede exceder los 500 caracteres");
        }
    }

    private void validarParticipacionUsuario(Usuario usuario, Desafio desafio) {
        if (!desafioServicio.usuarioParticipaEnDesafio(usuario.getId(), desafio.getId())) {
            throw new ParticipacionNoEncontradaException(usuario.getId(), desafio.getId());
        }
    }

    private void validarPermisosEdicion(Comentario comentario, Usuario usuario) {
        if (!comentario.getUsuario().equals(usuario)) {
            throw new AccesoNoAutorizadoException("editar", "comentario", comentario.getId());
        }
    }

    private void validarPermisosEliminacion(Comentario comentario, Usuario usuario) {
        if (!comentario.getUsuario().equals(usuario) &&
                !usuario.getRol().equals(Usuario.RolUsuario.ADMIN)) {
            throw new AccesoNoAutorizadoException("eliminar", "comentario", comentario.getId());
        }
    }
}