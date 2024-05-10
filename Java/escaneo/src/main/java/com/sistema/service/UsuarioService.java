package com.sistema.service;

import com.sistema.model.Usuario;
import com.sistema.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository; // Acceso a CRUD (borrar, modificar, crear, ver...)

    @Autowired
    private PasswordEncoder passwordEncoder; // Encriptar la contraseña *****

    public Usuario guardarUsuario(Usuario usuario) { // encripta la contraseña y guarda el usuario en la BBDD
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> obtenerUsuarioPorId(int id) { // Con esto obtenemos usuario por el id
        return usuarioRepository.findById(id);
    }

    public List<Usuario> listarUsuarios() { // listar todos los usuarios
        return usuarioRepository.findAllBy();
    }

    public void eliminarUsuarios(int id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> validarCredenciales(String email, String contrasena) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email); // busca en la BBDD si hay un email que coincidan lo cual si existe.
        if (usuario.isPresent() && passwordEncoder.matches(contrasena, usuario.get().getContrasena())) { // ve si el usuario existe y compara la contraseña con la que ya hay registarda en la BBDD
            return usuario;
        }
        return Optional.empty(); // si no optional vacio (caja optional)
    }
}
