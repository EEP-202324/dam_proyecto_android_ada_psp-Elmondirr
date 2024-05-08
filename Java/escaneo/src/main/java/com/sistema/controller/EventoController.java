package com.sistema.controller;

import com.sistema.model.Evento;
import com.sistema.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @GetMapping
    public ResponseEntity<List<Evento>> getAllEventos() {
        return ResponseEntity.ok(eventoService.listarEventos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> getEventoById(@PathVariable int id) {
        return eventoService.obtenerEventoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping({"/titulo"})
    public ResponseEntity<Evento> getEventoByTitulo(@PathVariable String titulo) {
        List<Evento> eventos = eventoService.buscarEventosPorTitulo(titulo);
        if(eventos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok((Evento) eventos);
        }
    }

    @PostMapping
    public ResponseEntity<Evento> createEvento(@RequestBody Evento evento) {
        return ResponseEntity.ok(eventoService.guardarEvento(evento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evento> updateEvento(@PathVariable int id, @RequestBody Evento evento) {
        return eventoService.obtenerEventoPorId(id)
                .map(u -> { // validar la existencia con el .map
                    evento.setId(id); // si ha encontrado un usuario hace la funcion del map
                    return ResponseEntity.ok(eventoService.guardarEvento(evento)); //si ha guardado 200
                })
                .orElse(ResponseEntity.notFound().build()); // si no 404
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvento(@PathVariable int id) {
        eventoService.eliminarEvento(id);
        return ResponseEntity.ok().build();
    }
}
