package com.sistema.dto;

/**
 * Clase DTO (Data Transfer Object) para manejar las solicitudes de registro de usuarios.
 */
public class RegisterRequest {

    // Nombre de usuario para el registro
    private String nombreUsuario;

    // Contraseña del usuario para el registro
    private String contrasena;

    // Email del usuario para el registro
    private String email;

    // Getters y setters

    /**
     * Obtiene el nombre de usuario.
     *
     * @return el nombre de usuario
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param nombreUsuario el nombre de usuario
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
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
}
