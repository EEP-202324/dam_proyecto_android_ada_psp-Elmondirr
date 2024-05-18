package com.sistema.service;

import com.sistema.dto.AuthResponse; // Importa la clase AuthResponse del paquete dto
import com.sistema.dto.LoginRequest; // Importa la clase LoginRequest del paquete dto
import com.sistema.dto.RegisterRequest; // Importa la clase RegisterRequest del paquete dto
import com.sistema.model.Usuario; // Importa la clase Usuario del paquete model
import com.sistema.repository.UsuarioRepository; // Importa la interfaz UsuarioRepository del paquete repository
import org.springframework.beans.factory.annotation.Autowired; // Importa la anotación Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importa la clase BCryptPasswordEncoder
import org.springframework.stereotype.Service; // Importa la anotación Service

import java.util.List; // Importa la clase List del paquete util
import java.util.Optional; // Importa la clase Optional del paquete util

@Service // Marca esta clase como un servicio de Spring
public class UsuarioService {

    @Autowired // Inyección de dependencias de Spring
    private UsuarioRepository usuarioRepository; // Repositorio para realizar operaciones CRUD en la base de datos

    @Autowired // Inyección de dependencias de Spring
    private BCryptPasswordEncoder passwordEncoder; // Codificador de contraseñas

    // Método para guardar un usuario en la base de datos
    public Usuario guardarUsuario(Usuario usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena())); // Encripta la contraseña del usuario
        return usuarioRepository.save(usuario); // Guarda el usuario en la base de datos y lo retorna
    }

    // Método para obtener un usuario por su ID
    public Optional<Usuario> obtenerUsuarioPorId(int id) {
        return usuarioRepository.findById(id); // Busca el usuario por ID y retorna un Optional
    }

    // Método para listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll(); // Retorna una lista con todos los usuarios
    }

    // Método para eliminar un usuario por su ID
    public void eliminarUsuario(int id) {
        usuarioRepository.deleteById(id); // Elimina el usuario por ID
    }

    // Método para validar las credenciales de un usuario
    public Optional<Usuario> validarCredenciales(String email, String contrasena) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email); // Busca el usuario por email
        if (usuario.isPresent() && passwordEncoder.matches(contrasena, usuario.get().getContrasena())) {
            return usuario; // Retorna el usuario si la contraseña es correcta
        }
        return Optional.empty(); // Retorna un Optional vacío si las credenciales no son válidas
    }

    // Método para obtener el usuario autenticado (ejemplo con ID 1)
    public Usuario obtenerUsuarioAutenticado() {
        return usuarioRepository.findById(1).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Método para actualizar el perfil de un usuario
    public Usuario actualizarPerfil(Usuario userProfile) {
        Usuario usuarioExistente = usuarioRepository.findById(userProfile.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuarioExistente.setNombre(userProfile.getNombre());
        usuarioExistente.setApellidos(userProfile.getApellidos());
        usuarioExistente.setEmail(userProfile.getEmail());
        usuarioExistente.setContrasena(userProfile.getContrasena());
        return usuarioRepository.save(usuarioExistente);
    }

    // Método para autenticar un usuario con sus credenciales
    public AuthResponse autenticar(LoginRequest loginRequest) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(loginRequest.getEmail()); // Busca el usuario por email
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get(); // Obtiene el usuario
            if (passwordEncoder.matches(loginRequest.getContrasena(), usuario.getContrasena())) {
                AuthResponse response = new AuthResponse(); // Crea una respuesta de autenticación
                response.setUsuario(usuario); // Asigna el usuario a la respuesta
                return response; // Retorna la respuesta de autenticación
            }
        }
        throw new RuntimeException("Email o contraseña incorrectos"); // Lanza una excepción si las credenciales no son válidas
    }

    // Método para registrar un nuevo usuario
    public AuthResponse registrar(RegisterRequest registerRequest) {
        if (usuarioRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya existe"); // Lanza una excepción si el email ya está registrado
        }

        Usuario usuario = new Usuario(); // Crea un nuevo usuario
        usuario.setNombre(registerRequest.getNombreUsuario()); // Asigna el nombre del usuario
        usuario.setContrasena(passwordEncoder.encode(registerRequest.getContrasena())); // Encripta y asigna la contraseña del usuario
        usuario.setEmail(registerRequest.getEmail()); // Asigna el email del usuario
        usuario.setRol("CLIENTE"); // Asigna el rol del usuario

        Usuario usuarioGuardado = usuarioRepository.save(usuario); // Guarda el usuario en la base de datos
        AuthResponse response = new AuthResponse(); // Crea una respuesta de autenticación
        response.setUsuario(usuarioGuardado); // Asigna el usuario guardado a la respuesta
        return response; // Retorna la respuesta de autenticación
    }
}
