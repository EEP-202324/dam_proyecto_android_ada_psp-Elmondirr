package com.sistema.dto;

import com.sistema.model.Usuario;

/**
 * Clase DTO (Data Transfer Object) para manejar las solicitudes de suscripción a eventos.
 */
public class SuscribirRequest {

    // ID del evento al que se quiere suscribir
    private int eventoId;

    // Usuario que se quiere suscribir al evento
    private Usuario usuario;

    // Getters y setters

    /**
     * Obtiene el ID del evento.
     *
     * @return el ID del evento
     */
    public int getEventoId() {
        return eventoId;
    }

    /**
     * Establece el ID del evento.
     *
     * @param eventoId el ID del evento
     */
    public void setEventoId(int eventoId) {
        this.eventoId = eventoId;
    }

    /**
     * Obtiene el usuario que se quiere suscribir al evento.
     *
     * @return el usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario que se quiere suscribir al evento.
     *
     * @param usuario el usuario
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
