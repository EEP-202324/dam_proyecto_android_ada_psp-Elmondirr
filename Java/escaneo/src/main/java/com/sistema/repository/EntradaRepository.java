package com.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sistema.model.Entrada;

import java.util.List;
import java.util.Optional;

public interface EntradaRepository extends JpaRepository<Entrada, Integer> {
    Optional<Entrada> findByUuid(String uuid); // buscar el codigo de la entrada
    List<Entrada> findByUsuarioId(int usuarioId); // Busca todas las entradas asociadas al usuario
}

