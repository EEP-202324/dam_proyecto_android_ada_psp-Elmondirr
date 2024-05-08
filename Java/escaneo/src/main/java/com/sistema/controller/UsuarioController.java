package com.sistema.controller;

import com.sistema.model.Usuario;
import com.sistema.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // indicamos que la clase es un controlador, nos retorna objetos de tipo JSON (serializacion)
@RequestMapping("/usuarios") // definir la ruta base que el controlador va a manejar (EndPoint)
public class UsuarioController {

    @Autowired // inyeccion de dependencias automaticamente (por el secutiry config)
    private UsuarioService usuarioService;

    @GetMapping // leer recursos (solicitud a la BBDD)
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios()); // encuentra todos los usuarios 200 ok
    }

    @GetMapping("/{id}") // solo da por el id
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable int id) { // coge el valor de la url y lo pasa como un int (para buscar el usuario por ese id)
        return usuarioService.obtenerUsuarioPorId(id)
                .map(ResponseEntity::ok) // me indica que to,do ha ido okey (200)
                .orElse(ResponseEntity.notFound().build()); // me indica que no ha ido bien y hay un error (404) y build construye la respuesta de que no se ha encontrado
    }

    @PostMapping // crear recursos
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) { // indica el cuerpo de la solicitud de http (deserializacion)
        return ResponseEntity.ok(usuarioService.guardarUsuario(usuario)); // guarda el usuario al crearse 200
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable int id, @RequestBody Usuario usuario) { // """aqui modificarlo"""
        return usuarioService.obtenerUsuarioPorId(id)
                .map(u -> { // validar la existencia con el .map
                    usuario.setId(id); // si ha encontrado un usuario hace la funcion del map
                    return ResponseEntity.ok(usuarioService.guardarUsuario(usuario)); //si ha guardado 200
                })
                .orElse(ResponseEntity.notFound().build()); // si no 404
    }

    @DeleteMapping("/{id}") //elimina recursos
    public ResponseEntity<Void> deleteUsuario(@PathVariable int id) {
        usuarioService.eliminarUsuarios(id); // si hay usuario
        return ResponseEntity.ok().build();  // lo borra ok 200
    }
}
