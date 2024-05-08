package com.sistema.service;

import com.sistema.model.Evento;
import com.sistema.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public Evento guardarEvento(Evento evento) { // crear o modificar el evento a la BBDD
        return eventoRepository.save(evento);
    }

    public Optional<Evento> obtenerEventoPorId(int id) { // Busca el evento por su Id
        return eventoRepository.findById(id);
    }

    public List<Evento> listarEventos(){ // lista todos los evento de la BBD
        return eventoRepository.findAllBy();
    }

    public List<Evento> buscarEventosPorTitulo(String titulo) { // encuentra los eventos que coincidan minimanete por el titulo
        return eventoRepository.findByTituloContaining(titulo);
    }

    public void eliminarEvento(int id) { // eliminar por id
        eventoRepository.deleteById(id);
    }
}
