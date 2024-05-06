package com.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sistema.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail (String email); // Busca el usuario por su email
}
