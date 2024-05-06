package com.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sistema.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email); // Busca el usuario por su email
    Optional<Usuario> findById(int id); // encontrar por el id.
    List<Usuario> findAllBy(); // Lista de todos los usuarios.
    Optional<Usuario> deleteById(int id);
}
