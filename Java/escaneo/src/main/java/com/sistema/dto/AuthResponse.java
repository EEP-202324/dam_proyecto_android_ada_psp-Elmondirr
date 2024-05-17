package com.sistema.dto;

import com.sistema.model.Usuario;

/**
 * Clase DTO (Data Transfer Object) para manejar las respuestas de autenticación.
 */
public class AuthResponse {

    // Objeto Usuario que representa al usuario autenticado
    private Usuario usuario;

    // Token de autenticación
    private String token;

    /**
     * Constructor que inicializa el objeto AuthResponse con un usuario y un token.
     *
     * @param usuario el objeto Usuario autenticado
     * @param token el token de autenticación
     */
    public AuthResponse(Usuario usuario, String token) {
        this.usuario = usuario;
        this.token = token;
    }

    /**
     * Constructor por defecto.
     */
    public AuthResponse() {
    }

    /**
     * Constructor que inicializa el objeto AuthResponse solo con un token.
     *
     * @param token el token de autenticación
     */
    public AuthResponse(String token) {
        this.token = token;
    }

    // Getters y setters

    /**
     * Obtiene el objeto Usuario.
     *
     * @return el usuario autenticado
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el objeto Usuario.
     *
     * @param usuario el usuario autenticado
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el token de autenticación.
     *
     * @return el token
     */
    public String getToken() {
        return token;
    }

    /**
     * Establece el token de autenticación.
     *
     * @param token el token
     */
    public void setToken(String token) {
        this.token = token;
    }
}
