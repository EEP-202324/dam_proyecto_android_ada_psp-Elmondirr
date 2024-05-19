package com.sistema.controller;

import com.sistema.model.Usuario;
import com.sistema.repository.UsuarioRepository;
import com.sistema.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.sistema.dto.AuthResponse;
import com.sistema.dto.LoginRequest;
import com.sistema.dto.RegisterRequest;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

/**
 * Controlador REST para manejar las operaciones relacionadas con los usuarios.
 */
@RestController // Indica que esta clase es un controlador REST, lo que permite manejar solicitudes HTTP y devolver respuestas en formato JSON.
@RequestMapping("/usuarios") // Define la ruta base que este controlador va a manejar (EndPoint).
public class UsuarioController {

    @Autowired // Inyección de dependencias, permite que Spring gestione la instancia del servicio.
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Maneja la solicitud de registro de un nuevo usuario.
     *
     * @param registerRequest Contiene los datos necesarios para registrar un usuario.
     * @return ResponseEntity con el AuthResponse.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        // Comprobamos que todos los campos necesarios estén presentes
        if (registerRequest.getNombreUsuario().isEmpty() ||
                registerRequest.getEmail().isEmpty() ||
                registerRequest.getContrasena().isEmpty()) {
            return ResponseEntity.badRequest().body("Faltan campos obligatorios");
        }

        // Creamos un nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(registerRequest.getNombreUsuario());
        usuario.setEmail(registerRequest.getEmail());
        usuario.setContrasena(passwordEncoder.encode(registerRequest.getContrasena()));
        usuario.setApellidos(registerRequest.getApellidos());

        // Guardamos el usuario en la base de datos
        Usuario newUser = usuarioRepository.save(usuario);
        return ResponseEntity.ok(new AuthResponse(newUser, "Registro exitoso"));
    }



    /**
     * Maneja la solicitud de autenticación de un usuario.
     *
     * @param loginRequest Contiene los datos necesarios para autenticar un usuario.
     * @return ResponseEntity con el AuthResponse.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<Usuario> optionalUser = usuarioRepository.findByEmail(loginRequest.getEmail());
        if (optionalUser.isPresent()) {
            Usuario existingUser = optionalUser.get();
            if (passwordEncoder.matches(loginRequest.getContrasena(), existingUser.getContrasena())) {
                return ResponseEntity.ok(new AuthResponse(existingUser, "Inicio de sesión exitoso"));
            }
        }
        return ResponseEntity.status(401).body("Email o contraseña inválidos");
    }

    /**
     * Obtiene el perfil del usuario autenticado.
     *
     * @return ResponseEntity con el usuario autenticado.
     */
    @GetMapping("/perfil/{id}")
    public ResponseEntity<Usuario> getUserProfile(@PathVariable String id) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado(id);
        return ResponseEntity.ok(usuario); // Devuelve una respuesta 200 OK con el usuario autenticado.
    }

    /**
     * Actualiza el perfil del usuario autenticado.
     *
     * @param userProfile Contiene los datos actualizados del perfil del usuario.
     * @return ResponseEntity con el usuario actualizado.
     */
    @PutMapping("/perfil")
    public ResponseEntity<Usuario> actualizarPerfil(@RequestBody Usuario userProfile) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarPerfil(userProfile);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Obtiene todos los usuarios.
     *
     * @return ResponseEntity con la lista de usuarios.
     */
    @GetMapping // Maneja solicitudes GET para leer recursos.
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios()); // Devuelve una respuesta 200 OK con la lista de usuarios.
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return ResponseEntity con el usuario o un 404 Not Found si no se encuentra.
     */
    @GetMapping("/{id}") // Maneja solicitudes GET para obtener un usuario por su ID.
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable int id) {
        return usuarioService.obtenerUsuarioPorId(id)
                .map(ResponseEntity::ok) // Devuelve una respuesta 200 OK si el usuario se encuentra.
                .orElse(ResponseEntity.notFound().build()); // Devuelve una respuesta 404 Not Found si no se encuentra el usuario.
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param usuario Contiene los datos del nuevo usuario.
     * @return ResponseEntity con el usuario creado.
     */
    @PostMapping // Maneja solicitudes POST para crear recursos.
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.guardarUsuario(usuario)); // Devuelve una respuesta 200 OK con el usuario creado.
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param id ID del usuario a actualizar.
     * @param usuario Contiene los datos actualizados del usuario.
     * @return ResponseEntity con el usuario actualizado o un 404 Not Found si no se encuentra.
     */
    @PutMapping("/{id}") // Maneja solicitudes PUT para actualizar recursos.
    public ResponseEntity<Usuario> updateUsuario(@PathVariable int id, @RequestBody Usuario usuario) {
        return usuarioService.obtenerUsuarioPorId(id)
                .map(u -> { // Valida la existencia del usuario con el método map.
                    usuario.setId(id); // Si se encuentra el usuario, se establece su ID.
                    return ResponseEntity.ok(usuarioService.guardarUsuario(usuario)); // Devuelve una respuesta 200 OK con el usuario actualizado.
                })
                .orElse(ResponseEntity.notFound().build()); // Devuelve una respuesta 404 Not Found si no se encuentra el usuario.
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return ResponseEntity vacío con un 200 OK.
     */
    @DeleteMapping("/{id}") // Maneja solicitudes DELETE para eliminar recursos.
    public ResponseEntity<Void> deleteUsuario(@PathVariable int id) {
        usuarioService.eliminarUsuario(id); // Elimina el usuario si existe.
        return ResponseEntity.ok().build(); // Devuelve una respuesta 200 OK vacía.
    }
}
