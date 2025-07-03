package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.Desafio;
import com.aroa.sportifyme.modelo.TipoActividad;
import com.aroa.sportifyme.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DesafioRepository extends JpaRepository<Desafio, Long> {

    // Métodos básicos que vienen de JpaRepository:
    // save(), findById(), findAll(), deleteById(), etc.

    List<Desafio> findByCreador(Usuario creador);

    List<Desafio> findByCreadorOrderByFechaInicioDesc(Usuario creador);

    List<Desafio> findByEsPublicoTrueAndFechaFinAfterOrderByFechaInicioAsc(LocalDateTime fechaActual);

    List<Desafio> findAllByOrderByFechaInicioDesc();

    // Consulta personalizada para buscar por tipo de actividad
    @Query("SELECT d FROM Desafio d WHERE d.tipoActividad = :tipo AND d.esPublico = true AND d.fechaFin > CURRENT_TIMESTAMP")
    List<Desafio> findDesafiosActivosPorTipo(TipoActividad tipo);

    // Consulta para verificar existencia
    boolean existsByIdAndCreador(Long id, Usuario creador);
}