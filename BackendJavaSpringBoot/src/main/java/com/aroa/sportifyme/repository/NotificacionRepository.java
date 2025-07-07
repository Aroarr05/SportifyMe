package com.aroa.sportifyme.repository;

import com.aroa.sportifyme.modelo.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);

    List<Notificacion> findByUsuarioIdAndLeidaFalseOrderByFechaCreacionDesc(Long usuarioId);

    Optional<Notificacion> findByIdAndUsuarioId(Long id, Long usuarioId);

    @Transactional
    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = true, n.fechaEdicion = :fecha WHERE n.usuario.id = :usuarioId AND n.leida = false")
    int marcarTodasComoLeidas(Long usuarioId, LocalDateTime fecha);

    @Query("SELECT COUNT(n) FROM Notificacion n WHERE n.usuario.id = :usuarioId")
    long countByUsuarioId(Long usuarioId);

    @Query("SELECT COUNT(n) FROM Notificacion n WHERE n.usuario.id = :usuarioId AND n.leida = false")
    long countByUsuarioIdAndLeidaFalse(Long usuarioId);


}