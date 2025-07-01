package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.Desafio;
import com.aroa.sportifyme.modelo.Usuario;
import java.util.List;
import java.util.Optional;

public interface DesafioDAO {
    // Cambia 'crear' por 'guardar' para coincidir con el servicio
    Desafio guardar(Desafio desafio);

    // Cambia 'encontrarPorId' por 'buscarPorId' para coincidir con el servicio
    Optional<Desafio> buscarPorId(Long id);

    List<Desafio> listarPorCreador(Usuario creador);
    void actualizar(Desafio desafio);
    void eliminar(Long id);
    List<Desafio> listarDesafiosActivos();
    List<Desafio> listarTodos();
}