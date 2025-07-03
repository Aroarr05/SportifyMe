package com.aroa.sportifyme.repository;

import com.aroa.sportifyme.modelo.Participacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipacionRepository extends JpaRepository<Participacion, Long> {

    // Equivalente a existsByUsuarioIdAndDesafioId
    boolean existsByUsuarioIdAndDesafioId(Long usuarioId, Long desafioId);

    // Equivalente a findByUsuarioId
    List<Participacion> findByUsuarioId(Long usuarioId);

    // Equivalente a findByDesafioId
    List<Participacion> findByDesafioId(Long desafioId);

    // findById ya está proporcionado por JpaRepository

    // deleteById ya está proporcionado por JpaRepository

    // Método adicional para obtener participaciones con información completa de usuario y desafío
    @Query("SELECT p FROM Participacion p JOIN FETCH p.usuario JOIN FETCH p.desafio WHERE p.id = :id")
    Optional<Participacion> findByIdWithUsuarioAndDesafio(@Param("id") Long id);

    // Método adicional para obtener participaciones de un usuario con información completa del desafío
    @Query("SELECT p FROM Participacion p JOIN FETCH p.desafio WHERE p.usuario.id = :usuarioId")
    List<Participacion> findByUsuarioIdWithDesafio(@Param("usuarioId") Long usuarioId);

    // Método adicional para obtener participaciones de un desafío con información completa del usuario
    @Query("SELECT p FROM Participacion p JOIN FETCH p.usuario WHERE p.desafio.id = :desafioId")
    List<Participacion> findByDesafioIdWithUsuario(@Param("desafioId") Long desafioId);
}