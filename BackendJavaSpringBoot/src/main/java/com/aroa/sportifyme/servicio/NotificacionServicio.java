package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.exception.RecursoNoEncontradoException;
import com.aroa.sportifyme.modelo.Notificacion;
import com.aroa.sportifyme.modelo.Usuario;
import com.aroa.sportifyme.repository.NotificacionRepository;
import com.aroa.sportifyme.seguridad.dto.NotificacionDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionServicio {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioServicio usuarioServicio;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public Notificacion crearNotificacion(Long usuarioId, String tipo, String mensaje, String enlace) {
        Usuario usuario = usuarioServicio.buscarPorId(usuarioId);

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setTipo(Notificacion.TipoNotificacion.valueOf(tipo));
        notificacion.setMensaje(mensaje);
        notificacion.setEnlace(enlace);
        notificacion.setLeida(false);
        notificacion.setFechaCreacion(LocalDateTime.now());

        Notificacion guardada = notificacionRepository.save(notificacion);
        enviarNotificacionEnTiempoReal(guardada);
        return guardada;
    }

    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesUsuario(Long usuarioId, boolean noLeidas) {
        return noLeidas
                ? notificacionRepository.findByUsuarioIdAndLeidaFalseOrderByFechaCreacionDesc(usuarioId)
                : notificacionRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId);
    }

    @Transactional(readOnly = true)
    public Notificacion obtenerNotificacionPorIdYUsuario(Long id, Long usuarioId) {
        return notificacionRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Notificación no encontrada"));
    }

    @Transactional
    public void marcarComoLeida(Long notificacionId, Long usuarioId) {
        Notificacion notificacion = obtenerNotificacionPorIdYUsuario(notificacionId, usuarioId);
        if (!notificacion.getLeida()) {
            notificacion.setLeida(true);
            notificacion.setFechaEdicion(LocalDateTime.now());
            notificacionRepository.save(notificacion);
        }
    }

    @Transactional
    public void marcarTodasComoLeidas(Long usuarioId) {
        notificacionRepository.marcarTodasComoLeidas(usuarioId, LocalDateTime.now());
    }

    @Transactional
    public void eliminarNotificacion(Long notificacionId, Long usuarioId) {
        Notificacion notificacion = obtenerNotificacionPorIdYUsuario(notificacionId, usuarioId);
        notificacionRepository.delete(notificacion);
    }

    @Transactional(readOnly = true)
    public long contarNotificacionesUsuario(Long usuarioId) {
        return notificacionRepository.countByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public long contarNotificacionesNoLeidasUsuario(Long usuarioId) {
        return notificacionRepository.countByUsuarioIdAndLeidaFalse(usuarioId);
    }

    private void enviarNotificacionEnTiempoReal(Notificacion notificacion) {
        try {
            messagingTemplate.convertAndSendToUser(
                    notificacion.getUsuario().getId().toString(),
                    "/queue/notificaciones",
                    new NotificacionDTO(notificacion)
            );
        } catch (Exception e) {
            // Loggear error pero no interrumpir el flujo principal
            System.err.println("Error enviando notificación WebSocket: " + e.getMessage());
        }
    }
}