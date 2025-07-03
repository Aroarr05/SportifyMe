package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.UsuarioLogro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioLogroRepository extends JpaRepository<UsuarioLogro, Long> {
    List<UsuarioLogro> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioIdAndLogroId(Long usuarioId, Long logroId);

    // Método para buscar por criterio específico
    boolean existsByUsuarioIdAndLogroCriterio(Long usuarioId, String criterio);
}