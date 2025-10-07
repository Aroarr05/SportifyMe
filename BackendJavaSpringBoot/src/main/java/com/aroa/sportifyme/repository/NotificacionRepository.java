package com.aroa.sportifyme.repository;

import com.aroa.sportifyme.modelo.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    
    // ✅ Métodos que YA tienes (mantener)
    List<Notificacion> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);
    List<Notificacion> findByUsuarioIdAndLeidaFalseOrderByFechaCreacionDesc(Long usuarioId);
    Optional<Notificacion> findByIdAndUsuarioId(Long id, Long usuarioId);
    long countByUsuarioId(Long usuarioId);
    long countByUsuarioIdAndLeidaFalse(Long usuarioId);
    
    // ✅ AGREGAR este método que FALTA
    List<Notificacion> findByUsuarioIdAndLeidaFalse(Long usuarioId);
    
    // ✅ OPCIONAL: Método más eficiente para marcar todas como leídas
    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = true WHERE n.usuario.id = :usuarioId AND n.leida = false")
    int marcarTodasComoLeidasPorUsuario(@Param("usuarioId") Long usuarioId);
}