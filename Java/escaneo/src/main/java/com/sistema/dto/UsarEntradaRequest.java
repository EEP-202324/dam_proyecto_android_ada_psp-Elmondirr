package com.sistema.dto;

/**
 * Clase DTO (Data Transfer Object) para manejar las solicitudes de usar una entrada.
 */
public class UsarEntradaRequest {

    // Identificador de la entrada a ser usada
    private int entradaId;

    // Getters y setters

    /**
     * Obtiene el identificador de la entrada.
     *
     * @return el identificador de la entrada
     */
    public int getEntradaId() {
        return entradaId;
    }

    /**
     * Establece el identificador de la entrada.
     *
     * @param entradaId el nuevo identificador de la entrada
     */
    public void setEntradaId(int entradaId) {
        this.entradaId = entradaId;
    }
}
