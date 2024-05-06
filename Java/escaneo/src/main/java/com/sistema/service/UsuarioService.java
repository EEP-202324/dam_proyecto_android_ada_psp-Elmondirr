package com.sistema.service;

import com.sistema.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository; // Acceso a CRUD (borrar, modificar, crear, ver...)

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Encriptar la contraseña *****
}
