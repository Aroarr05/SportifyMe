package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.modelo.*;
import com.aroa.sportifyme.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogroServicio {

    private final LogroRepository logroRepository;
    private final UsuarioLogroRepository usuarioLogroRepository;
    private final UsuarioServicio usuarioServicio;
    private final NotificacionServicio notificacionServicio;

    @Transactional
    public void desbloquearLogro(Long usuarioId, String criterio) {
        if (!usuarioLogroRepository.existsByUsuarioIdAndLogroCriterio(usuarioId, criterio)) {
            Logro logro = logroRepository.findByCriterio(criterio)
                    .orElseThrow(() -> new IllegalArgumentException("Logro no encontrado"));

            Usuario usuario = usuarioServicio.buscarPorId(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            UsuarioLogro usuarioLogro = new UsuarioLogro(usuario, logro);
            usuarioLogroRepository.save(usuarioLogro);

            // Enviar notificación
            notificacionServicio.crearNotificacion(
                    usuarioId,
                    "LOGRO",
                    "¡Felicidades! Has obtenido el logro: " + logro.getNombre()
            );
        }
    }

    public List<UsuarioLogro> obtenerLogrosDeUsuario(Long usuarioId) {
        return usuarioLogroRepository.findByUsuarioId(usuarioId);
    }
}