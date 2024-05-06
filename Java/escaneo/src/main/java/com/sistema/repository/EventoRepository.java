package com.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sistema.model.Evento;
import java.util.List;
import java.util.Optional;

public interface EventoRepository extends JpaRepository<Evento, Integer> {
    List<Evento> findAllBy(); // Da la lista de todos los eventos de la BBDD
    List<Evento> findByTituloContaining(String titulo); // si coincide con letras me lo busca
    Optional<Evento> findById(int id); // encontrar por id
}

