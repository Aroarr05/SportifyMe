package com.aroa.sportifyme.repository;

import com.aroa.sportifyme.modelo.Progreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgresoRepository extends JpaRepository<Progreso, Long> {

    // Equivalente a registrarProgreso - save() ya está proporcionado por JpaRepository

    // Equivalente a obtenerProgresosPorDesafio con información completa
    @Query("SELECT p FROM Progreso p JOIN FETCH p.usuario JOIN FETCH p.desafio " +
            "WHERE p.desafio.id = :desafioId ORDER BY p.fechaRegistro DESC")
    List<Progreso> findByDesafioIdWithUsuarioAndDesafio(@Param("desafioId") Long desafioId);

    // Equivalente a obtenerRankingDesafio
    @Query("SELECT p FROM Progreso p JOIN FETCH p.usuario JOIN FETCH p.desafio " +
            "WHERE p.desafio.id = :desafioId AND p.valorActual = (" +
            "SELECT MAX(p2.valorActual) FROM Progreso p2 WHERE p2.desafio.id = :desafioId AND p2.usuario.id = p.usuario.id) " +
            "ORDER BY p.valorActual DESC")
    List<Progreso> findRankingByDesafioId(@Param("desafioId") Long desafioId);

    // Equivalente a buscarPorId con información completa
    @Query("SELECT p FROM Progreso p JOIN FETCH p.usuario JOIN FETCH p.desafio WHERE p.id = :id")
    Optional<Progreso> findByIdWithUsuarioAndDesafio(@Param("id") Long id);

    // Métodos adicionales útiles
    List<Progreso> findByUsuarioId(Long usuarioId);

    List<Progreso> findByUsuarioIdAndDesafioId(Long usuarioId, Long desafioId);

    @Query("SELECT MAX(p.valorActual) FROM Progreso p WHERE p.usuario.id = :usuarioId AND p.desafio.id = :desafioId")
    Optional<Double> findMaxValorActualByUsuarioAndDesafio(@Param("usuarioId") Long usuarioId,
                                                           @Param("desafioId") Long desafioId);
}