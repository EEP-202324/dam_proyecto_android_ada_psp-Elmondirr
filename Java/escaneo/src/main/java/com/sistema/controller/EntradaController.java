package com.sistema.controller;


import com.sistema.model.Entrada;
import com.sistema.model.Evento;
import com.sistema.service.EntradaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entradas")
public class EntradaController {

    @Autowired
    private EntradaService entradaService;

    @GetMapping
    public ResponseEntity<List<Entrada>> getAllEntradas() {
        return ResponseEntity.ok(entradaService.listarEntradas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Entrada>> getEntradaById(@PathVariable int id) {
        List<Entrada> entradas = entradaService.listarEntradasPorUsuarioId(id);
        if (entradas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(entradas);
    }

    @PostMapping
    public ResponseEntity<Entrada> createEntrada(@RequestBody Entrada entrada) {
        return ResponseEntity.ok(entradaService.guardarEntrada(entrada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entrada> updateEntrada(@PathVariable int id, @RequestBody Entrada entrada) {
        return entradaService.obtenerEntradaPorId(id)
                .map(u -> { // validar la existencia con el .map
                    entrada.setId(id); // si ha encontrado un usuario hace la funcion del map
                    return ResponseEntity.ok(entradaService.guardarEntrada(entrada)); //si ha guardado 200
                })
                .orElse(ResponseEntity.notFound().build()); // si no 404
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntrada(@PathVariable int id) {
        entradaService.eliminarEntrada(id);
        return ResponseEntity.ok().build();
    }
}
