    package com.sistema.service;

    import com.sistema.model.Entrada;
    import com.sistema.model.Evento;
    import com.sistema.model.Usuario;
    import com.sistema.repository.EntradaRepository;
    import com.sistema.repository.EventoRepository;
    import com.sistema.repository.UsuarioRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;

    @Service
    public class EntradaService {
        @Autowired
        private EntradaRepository entradaRepository;
        @Autowired
        private UsuarioRepository usuarioRepository;
        @Autowired
        private EventoRepository eventoRepository;


        public Entrada guardarEntrada(int usuarioId, int eventoId) { // crea o modifica la entrada
    // Buscar usuario y evento
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            Evento evento = eventoRepository.findById(eventoId)
                    .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

            // Crear o modificar la entrada
            Entrada entrada = new Entrada();
            entrada.setUsuario(usuario);
            entrada.setEvento(evento);
            return entradaRepository.save(entrada);
        }

        public List<Entrada> listarEntradasPorUsuarioId(int id) { // lista las entradas del usuario (ver las entradas que tienes)
            return entradaRepository.findByUsuarioId(id);
        }

        public List<Entrada> listarEntradas() {
            return entradaRepository.findAll();
        }

        public Optional<Entrada> obtenerEntradaPorId(int id) {
            return entradaRepository.findById(id);
        }

        public void eliminarEntrada(int id) { // eliminar la entrada
            entradaRepository.deleteById(id);
        }

        public Optional<Entrada> verificarEntradaPorUuid(String uuid) { // verificacion para "pasar por la puerta" (boton)
            Optional<Entrada> entrada = entradaRepository.findByUuid(uuid);
            return entrada;
        }
    }
