package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.modelo.*;
import com.aroa.sportifyme.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionServicio {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioServicio usuarioServicio;

    @Transactional
    public Notificacion crearNotificacion(Long usuarioId, String tipo, String mensaje) {
        Usuario usuario = usuarioServicio.buscarPorId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setTipo(tipo);
        notificacion.setMensaje(mensaje);
        notificacion.setLeida(false);
        notificacion.setFechaCreacion(LocalDateTime.now());

        return notificacionRepository.save(notificacion);
    }

    @Transactional
    public void marcarComoLeida(Long notificacionId, Long usuarioId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada"));

        if (!notificacion.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("No tienes permiso para modificar esta notificación");
        }

        notificacion.setLeida(true);
        notificacionRepository.save(notificacion);
    }

    public List<Notificacion> obtenerNotificacionesNoLeidas(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdAndLeidaFalse(usuarioId);
    }
}