package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.modelo.*;
import com.aroa.sportifyme.dao.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioServicio {

    private final ComentarioRepository comentarioRepository;
    private final UsuarioServicio usuarioServicio;
    private final DesafioServicio desafioServicio;

    @Transactional
    public Comentario crearComentario(Long desafioId, String contenido, String emailUsuario) {
        // Validación de entrada
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido del comentario no puede estar vacío");
        }

        Usuario usuario = usuarioServicio.buscarPorEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Verificación alternativa si no quieres añadir existeDesafio
        Desafio desafio = desafioServicio.buscarPorId(desafioId)
                .orElseThrow(() -> new IllegalArgumentException("Desafío no encontrado"));

        Comentario comentario = new Comentario();
        comentario.setContenido(contenido);
        comentario.setAutor(usuario);
        comentario.setDesafio(desafio);
        comentario.setFechaCreacion(LocalDateTime.now());

        return comentarioRepository.save(comentario);
    }

    @Transactional
    public void eliminarComentario(Long comentarioId, String emailUsuario) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado"));

        // Corregido: faltaba paréntesis de cierre en el if
        if (!comentario.getAutor().getEmail().equals(emailUsuario)) {
            throw new SecurityException("No tienes permiso para eliminar este comentario");
        }

        comentarioRepository.delete(comentario);
    }

    public List<Comentario> obtenerComentariosPorDesafio(Long desafioId) {
        // Opción 1: Si añades el método existeDesafio
        // if (!desafioServicio.existeDesafio(desafioId)) {
        //     throw new IllegalArgumentException("El desafío especificado no existe");
        // }

        // Opción 2: Alternativa sin necesidad de existeDesafio
        try {
            desafioServicio.buscarPorId(desafioId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El desafío especificado no existe");
        }

        return comentarioRepository.findByDesafioIdOrderByFechaCreacionDesc(desafioId);
    }
    public Usuario buscarPorId(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}