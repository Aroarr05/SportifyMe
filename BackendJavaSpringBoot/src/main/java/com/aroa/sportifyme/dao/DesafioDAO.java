package com.aroa.sportifyme.dao;

import com.aroa.sportifyme.modelo.Desafio;
import com.aroa.sportifyme.modelo.Usuario;
import java.util.List;

public interface DesafioDAO {
    void crear(Desafio desafio);
    Desafio encontrarPorId(Long id);
    List<Desafio> listarPorCreador(Usuario creador);
    void actualizar(Desafio desafio);
    void eliminar(Long id);
}