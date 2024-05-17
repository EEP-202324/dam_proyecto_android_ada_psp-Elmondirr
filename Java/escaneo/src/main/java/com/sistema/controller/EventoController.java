package com.sistema.controller;

import com.sistema.model.Evento;
import com.sistema.service.EventoService;
import com.sistema.dto.SuscribirRequest;
import com.sistema.dto.SuscribirResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para manejar las operaciones relacionadas con los eventos.
 */
@RestController
@RequestMapping("/eventos")
public class EventoController {

    @Autowired // Inyección de dependencias para utilizar el servicio de eventos.
    private EventoService eventoService;

    /**
     * Obtiene todos los eventos.
     *
     * @return ResponseEntity con la lista de eventos.
     */
    @GetMapping
    public ResponseEntity<List<Evento>> getAllEventos() {
        return ResponseEntity.ok(eventoService.listarEventos()); // Devuelve una respuesta 200 OK con la lista de eventos.
    }

    /**
     * Obtiene un evento por su ID.
     *
     * @param id ID del evento.
     * @return ResponseEntity con el evento o un 404 Not Found si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Evento> getEventoById(@PathVariable int id) {
        return eventoService.obtenerEventoPorId(id)
                .map(ResponseEntity::ok) // Devuelve una respuesta 200 OK si el evento se encuentra.
                .orElse(ResponseEntity.notFound().build()); // Devuelve una respuesta 404 Not Found si no se encuentra el evento.
    }

    /**
     * Suscribe a un usuario a un evento.
     *
     * @param request Contiene los datos necesarios para suscribir a un evento.
     * @return ResponseEntity con el resultado de la suscripción.
     */
    @PostMapping("/suscribir")
    public ResponseEntity<SuscribirResponse> subscribeToEvent(@RequestBody SuscribirRequest request) {
        boolean exito = eventoService.suscribirEvento(request.getEventoId());
        SuscribirResponse response = new SuscribirResponse();
        response.setExito(exito);
        return ResponseEntity.ok(response); // Devuelve una respuesta 200 OK con el resultado de la suscripción.
    }

    /**
     * Obtiene eventos por título.
     *
     * @param titulo Título del evento.
     * @return ResponseEntity con la lista de eventos o un 204 No Content si no se encuentra.
     */
    @GetMapping("/titulo")
    public ResponseEntity<Evento> getEventoByTitulo(@PathVariable String titulo) {
        List<Evento> eventos = eventoService.buscarEventosPorTitulo(titulo);
        if (eventos.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve una respuesta 204 No Content si no se encuentran eventos.
        }
        return ResponseEntity.ok((Evento) eventos); // Devuelve una respuesta 200 OK con la lista de eventos.
    }

    /**
     * Crea un nuevo evento.
     *
     * @param evento Contiene los datos del nuevo evento.
     * @return ResponseEntity con el evento creado.
     */
    @PostMapping
    public ResponseEntity<Evento> createEvento(@RequestBody Evento evento) {
        return ResponseEntity.ok(eventoService.guardarEvento(evento)); // Devuelve una respuesta 200 OK con el evento creado.
    }

    /**
     * Actualiza un evento existente.
     *
     * @param id ID del evento a actualizar.
     * @param evento Contiene los datos actualizados del evento.
     * @return ResponseEntity con el evento actualizado o un 404 Not Found si no se encuentra.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Evento> updateEvento(@PathVariable int id, @RequestBody Evento evento) {
        return eventoService.obtenerEventoPorId(id)
                .map(u -> { // Valida la existencia del evento con el método map.
                    evento.setId(id); // Si se encuentra el evento, se establece su ID.
                    return ResponseEntity.ok(eventoService.guardarEvento(evento)); // Devuelve una respuesta 200 OK con el evento actualizado.
                })
                .orElse(ResponseEntity.notFound().build()); // Devuelve una respuesta 404 Not Found si no se encuentra el evento.
    }

    /**
     * Elimina un evento por su ID.
     *
     * @param id ID del evento a eliminar.
     * @return ResponseEntity vacío con un 200 OK.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvento(@PathVariable int id) {
        eventoService.eliminarEvento(id); // Elimina el evento si existe.
        return ResponseEntity.ok().build(); // Devuelve una respuesta 200 OK vacía.
    }
}
