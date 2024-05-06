package com.sistema.service;

import com.sistema.model.Entrada;
import com.sistema.repository.EntradaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntradaService {
    @Autowired
    private EntradaRepository entradaRepository;

    public Entrada guardarEntrada(Entrada entrada) { // crea o modifica la entrada
        return entradaRepository.save(entrada);
    }

    public List<Entrada> listarEntradasPorUsuarioId(int id) { // lista las entradas del usuario (ver las entradas que tienes)
        return entradaRepository.findByUsuarioId(id);
    }

    public void eliminarEntrada(int id) { // eliminar la entrada
        entradaRepository.deleteById(id);
    }

    public Optional<Entrada> verificarEntradaPorUuid(String uuid) { // verificacion para "pasar por la puerta" (boton)
        Optional<Entrada> entrada = entradaRepository.findByUuid(uuid);
        return entrada;
    }
}
