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


        // Método para guardar una entrada en la base de datos
        public Entrada guardarEntrada(int usuarioId, int eventoId, String titulo) {
            // Busca el usuario por ID y lanza una excepción si no se encuentra
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Busca el evento por ID y lanza una excepción si no se encuentra
            Evento evento = eventoRepository.findById(eventoId)
                    .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

            // Crear una nueva entrada
            Entrada entrada = new Entrada();
            entrada.setUsuario(usuario); // Asigna el usuario a la entrada
            entrada.setEvento(evento); // Asigna el evento a la entrada
            entrada.setUuid(generateUUID()); // Genera y asigna un UUID a la entrada
            entrada.setTitulo(titulo); // genera titulo
            return entradaRepository.save(entrada); // Guarda la entrada en la base de datos y la retorna
        }

        // Método para marcar una entrada como usada
        public boolean usarEntrada(int entradaId) {
            Optional<Entrada> entradaOpt = entradaRepository.findById(entradaId); // Busca la entrada por ID
            if (entradaOpt.isPresent()) { // Verifica si la entrada existe
                Entrada entrada = entradaOpt.get();
                // Implementa la lógica para marcar la entrada como usada
                entradaRepository.save(entrada); // Guarda la entrada actualizada
                return true; // Retorna true si la operación fue exitosa
            }
            return false; // Retorna false si la entrada no existe
        }

        // Método privado para generar un UUID
        private String generateUUID() {
            // Utiliza la clase UUID para generar un UUID único
            return java.util.UUID.randomUUID().toString();
        }

        // Método para suscribir a un usuario a un evento
        public boolean suscribirAEvento(int eventoId, int usuarioId) {
            Evento evento = eventoRepository.findById(eventoId).orElseThrow(() -> new RuntimeException("Evento no encontrado")); // Busca el evento
            Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no encontrado")); // Busca el usuario

            Entrada entrada = new Entrada();
            entrada.setEvento(evento); // Asigna el evento a la entrada
            entrada.setUsuario(usuario); // Asigna el usuario a la entrada
            entradaRepository.save(entrada); // Guarda la entrada en la base de datos
            return true; // Retorna true si la operación fue exitosa
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

        // Método para verificar una entrada por su UUID
        public Optional<Entrada> verificarEntradaPorUuid(String uuid) {
            return entradaRepository.findByUuid(uuid); // Retorna un Optional con la entrada encontrada
        }
    }
