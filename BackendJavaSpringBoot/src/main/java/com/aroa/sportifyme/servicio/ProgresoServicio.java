package com.aroa.sportifyme.servicio;

import com.aroa.sportifyme.dto.ProgresoDTO;
import com.aroa.sportifyme.dto.RankingDTO;
import com.aroa.sportifyme.modelo.*;
import com.aroa.sportifyme.dao.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgresoServicio {

    private final ProgresoDAO progresoDAO;
    private final UsuarioServicio usuarioServicio;
    private final DesafioServicio desafioServicio;
    private final ParticipacionServicio participacionServicio;

    @Transactional
    public Progreso registrarProgreso(ProgresoDTO progresoDTO, String usuarioEmail) {
        // Validar que el usuario existe
        Usuario usuario = usuarioServicio.buscarPorEmail(usuarioEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Validar que el desafío existe
        Desafio desafio = desafioServicio.buscarPorId(progresoDTO.getDesafioId())
                .orElseThrow(() -> new IllegalArgumentException("Desafío no encontrado"));

        // Validar que el usuario está participando en el desafío
        if (!participacionServicio.usuarioParticipaEnDesafio(usuario.getId(), desafio.getId())) {
            throw new IllegalArgumentException("El usuario no está participando en este desafío");
        }

        // Validar que el desafío no ha expirado
        if (LocalDateTime.now().isAfter(desafio.getFechaFin())) {
            throw new IllegalArgumentException("El desafío ya ha finalizado");
        }

        // Crear y guardar el progreso
        Progreso progreso = new Progreso();
        progreso.setValorActual(progresoDTO.getValorActual());
        progreso.setComentario(progresoDTO.getComentario());
        progreso.setUsuario(usuario);
        progreso.setDesafio(desafio);
        progreso.setFechaRegistro(LocalDateTime.now());

        return progresoDAO.registrarProgreso(progreso);
    }

    public List<Progreso> obtenerProgresosPorDesafio(Long desafioId) {
        return progresoDAO.obtenerProgresosPorDesafio(desafioId);
    }

    public List<RankingDTO> generarRankingDesafio(Long desafioId) {
        List<Progreso> progresos = progresoDAO.obtenerRankingDesafio(desafioId);

        return progresos.stream()
                .map(progreso -> {
                    RankingDTO dto = new RankingDTO();
                    dto.setUsuarioId(progreso.getUsuario().getId());
                    dto.setNombreUsuario(progreso.getUsuario().getNombre());
                    dto.setValorActual(progreso.getValorActual());
                    // Puedes agregar más datos del usuario si es necesario
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Optional<Progreso> buscarProgresoPorId(Long id) {
        return progresoDAO.buscarPorId(id);
    }
}