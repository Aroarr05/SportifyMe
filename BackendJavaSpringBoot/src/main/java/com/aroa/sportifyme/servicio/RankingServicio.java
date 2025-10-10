package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.seguridad.dto.RankingDTO;
import com.aroa.sportifyme.repository.RankingRepository;
import com.aroa.sportifyme.repository.ProgresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingServicio {

    private final RankingRepository rankingRepository;
    private final ProgresoRepository progresoRepository;
    private final DesafioServicio desafioServicio;

    @Transactional(readOnly = true)
    public List<RankingDTO> obtenerRankingDesafio(Long desafioId) {
        try {
            // Verificar que el desafío existe
            if (desafioServicio.buscarPorId(desafioId) == null) {
                throw new IllegalArgumentException("Desafío no encontrado");
            }

            // Obtener ranking básico
            List<RankingDTO> rankingBasico = rankingRepository.findSimpleRankingByDesafioId(desafioId);
            
            // Enriquecer los datos con información adicional
            return rankingBasico.stream()
                    .map(ranking -> enriquecerDatosRanking(ranking, desafioId))
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener ranking del desafío: " + e.getMessage(), e);
        }
    }

    private RankingDTO enriquecerDatosRanking(RankingDTO ranking, Long desafioId) {
        Long usuarioId = ranking.getUsuario().getId();
        
        // Calcular desafíos completados
        Integer desafiosCompletados = contarDesafiosCompletados(usuarioId);
        
        // Calcular progreso total para este desafío específico
        BigDecimal progresoTotal = calcularProgresoTotal(usuarioId, desafioId);
        
        // Actualizar el objeto ranking con los datos enriquecidos
        ranking.setDesafiosCompletados(desafiosCompletados);
        ranking.setProgresoTotal(progresoTotal);
        
        return ranking;
    }

    private Integer contarDesafiosCompletados(Long usuarioId) {
        try {
            return progresoRepository.countDesafiosCompletadosByUsuario(usuarioId);
        } catch (Exception e) {
            return 0; // Valor por defecto en caso de error
        }
    }

    private BigDecimal calcularProgresoTotal(Long usuarioId, Long desafioId) {
        try {
            BigDecimal progreso = progresoRepository.calcularProgresoUsuarioDesafio(usuarioId, desafioId);
            return progreso != null ? progreso.min(BigDecimal.valueOf(100)) : BigDecimal.ZERO;
        } catch (Exception e) {
            return BigDecimal.ZERO; // Valor por defecto en caso de error
        }
    }

    @Transactional(readOnly = true)
    public List<RankingDTO> obtenerRankingGlobal() {
        try {
            return rankingRepository.findGlobalRanking();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener ranking global: " + e.getMessage(), e);
        }
    }
}