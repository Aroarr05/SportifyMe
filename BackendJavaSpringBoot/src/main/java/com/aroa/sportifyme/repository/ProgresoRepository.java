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

    @Query("SELECT p FROM Progreso p JOIN FETCH p.usuario JOIN FETCH p.desafio " +
            "WHERE p.desafio.id = :desafioId ORDER BY p.fechaRegistro DESC")
    List<Progreso> findByDesafioIdWithUsuarioAndDesafio(@Param("desafioId") Long desafioId);

    @Query("SELECT p FROM Progreso p JOIN FETCH p.usuario " +
            "WHERE p.desafio.id = :desafioId " +
            "ORDER BY p.valorActual DESC")
    List<Progreso> findRankingByDesafioId(@Param("desafioId") Long desafioId);

    @Query("SELECT p FROM Progreso p JOIN FETCH p.usuario JOIN FETCH p.desafio WHERE p.id = :id")
    Optional<Progreso> findByIdWithUsuarioAndDesafio(@Param("id") Long id);

    @Query("SELECT COUNT(p) > 0 FROM Progreso p " +
            "WHERE p.usuario.id = :usuarioId AND p.desafio.id = :desafioId")
    boolean existsByUsuarioAndDesafio(@Param("usuarioId") Long usuarioId,
                                      @Param("desafioId") Long desafioId);
}