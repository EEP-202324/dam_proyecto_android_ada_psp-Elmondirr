package com.sistema.dto;

/**
 * Clase DTO (Data Transfer Object) para manejar las respuestas de suscripción a eventos.
 */
public class SuscribirResponse {

    // Indica si la suscripción fue exitosa
    private boolean exito;

    /**
     * Constructor con parámetros.
     *
     * @param exito indica si la suscripción fue exitosa
     */
    public SuscribirResponse(boolean exito) {
        this.exito = exito;
    }

    /**
     * Constructor sin parámetros.
     */
    public SuscribirResponse() {
    }

    // Getters y setters

    /**
     * Obtiene el estado de la suscripción.
     *
     * @return true si la suscripción fue exitosa, false en caso contrario
     */
    public boolean isExito() {
        return exito;
    }

    /**
     * Establece el estado de la suscripción.
     *
     * @param exito true si la suscripción fue exitosa, false en caso contrario
     */
    public void setExito(boolean exito) {
        this.exito = exito;
    }
}
