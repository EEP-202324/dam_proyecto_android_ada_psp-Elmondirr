package com.sistema.dto;

/**
 * Clase DTO (Data Transfer Object) para manejar las solicitudes de inicio de sesión de usuarios.
 */
public class LoginRequest {

    // Email del usuario para el inicio de sesión
    private String email;

    // Contraseña del usuario para el inicio de sesión
    private String contrasena;

    // Getters y Setters

    /**
     * Obtiene el email del usuario.
     *
     * @return el email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el email del usuario.
     *
     * @param email el email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return la contraseña
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param contrasena la contraseña
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
