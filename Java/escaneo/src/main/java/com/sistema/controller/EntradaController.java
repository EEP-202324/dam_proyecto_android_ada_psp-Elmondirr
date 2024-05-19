package com.sistema.controller;

import com.sistema.dto.SuscribirRequest;
import com.sistema.dto.SuscribirResponse;
import com.sistema.model.Entrada;
import com.sistema.dto.UsarEntradaRequest;
import com.sistema.service.EntradaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para manejar las operaciones relacionadas con las entradas.
 */
@RestController
@RequestMapping("/entradas")
public class EntradaController {

    @Autowired // Inyección de dependencias para utilizar el servicio de entradas.
    private EntradaService entradaService;

    /**
     * Obtiene todas las entradas.
     *
     * @return ResponseEntity con la lista de todas las entradas.
     */
    @GetMapping
    public ResponseEntity<List<Entrada>> getAllEntradas() {
        return ResponseEntity.ok(entradaService.listarEntradas()); // Devuelve una respuesta 200 OK con la lista de entradas.
    }

    /**
     * Obtiene las entradas por ID de usuario.
     *
     * @param id ID del usuario.
     * @return ResponseEntity con la lista de entradas del usuario o un 204 No Content si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<Entrada>> getEntradaById(@PathVariable int id) {
        List<Entrada> entradas = entradaService.listarEntradasPorUsuarioId(id);
        if (entradas.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve una respuesta 204 No Content si no se encuentran entradas.
        }
        return ResponseEntity.ok(entradas); // Devuelve una respuesta 200 OK con la lista de entradas del usuario.
    }

    /**
     * Suscribe a un usuario a un evento.
     *
     * @param request Contiene los datos necesarios para suscribir a un evento.
     * @return ResponseEntity con el resultado de la suscripción.
     */
    @PostMapping("/suscribir")
    public ResponseEntity<SuscribirResponse> suscribirAEvento(@RequestBody SuscribirRequest request) {
        boolean exito = entradaService.suscribirAEvento(request.getEventoId(), request.getUsuario().getId());
        return ResponseEntity.ok(new SuscribirResponse(exito)); // Devuelve una respuesta 200 OK con el resultado de la suscripción.
    }

    /**
     * Crea una nueva entrada.
     *
     * @param usuarioId ID del usuario.
     * @param eventoId ID del evento.
     * @return ResponseEntity con la entrada creada o un 400 Bad Request si ocurre un error.
     */
    @PostMapping
    public ResponseEntity<?> createEntrada(@RequestParam int usuarioId, @RequestParam int eventoId, @RequestParam String titulo) {
        try {
            Entrada entrada = entradaService.guardarEntrada(usuarioId, eventoId, titulo);
            return ResponseEntity.ok(entrada); // Devuelve una respuesta 200 OK con la entrada creada.
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Devuelve una respuesta 400 Bad Request si ocurre un error.
        }
    }

    /**
     * Marca una entrada como usada.
     *
     * @param request Contiene los datos necesarios para usar una entrada.
     * @return ResponseEntity con el resultado de usar la entrada.
     */
    @PostMapping("/usar")
    public ResponseEntity<Map<String, Boolean>> useTicket(@RequestBody UsarEntradaRequest request) {
        boolean exito = entradaService.usarEntrada(request.getEntradaId());
        Map<String, Boolean> response = new HashMap<>();
        response.put("exito", exito);
        return ResponseEntity.ok(response); // Devuelve una respuesta 200 OK con el resultado de usar la entrada.
    }

    /**
     * Actualiza una entrada existente.
     *
     * @param id ID de la entrada a actualizar.
     * @param entrada Contiene los datos actualizados de la entrada.
     * @return ResponseEntity con la entrada actualizada o un 404 Not Found si no se encuentra.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Entrada> updateEntrada(@PathVariable int id, @RequestBody Entrada entrada) {
        return entradaService.obtenerEntradaPorId(id)
                .map(u -> {
                    entrada.setId(id); // Si se encuentra la entrada, se establece su ID.
                    return ResponseEntity.ok(entradaService.guardarEntrada(entrada.getUsuario().getId(), entrada.getEvento().getId(), entrada.getTitulo())); // Devuelve una respuesta 200 OK con la entrada actualizada.
                })
                .orElse(ResponseEntity.notFound().build()); // Devuelve una respuesta 404 Not Found si no se encuentra la entrada.
    }

    /**
     * Elimina una entrada por su ID.
     *
     * @param id ID de la entrada a eliminar.
     * @return ResponseEntity vacío con un 200 OK.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntrada(@PathVariable int id) {
        entradaService.eliminarEntrada(id); // Elimina la entrada si existe.
        return ResponseEntity.ok().build(); // Devuelve una respuesta 200 OK vacía.
    }
}
