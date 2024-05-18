package com.example.appeventos.model

// Definición de la clase de datos UsarEntradaRequest
data class UsarEntradaRequest(
    val entradaId: Int, // Identificador de la entrada que se desea usar
    val usuarioId: Int // Identificador del usuario que desea usar la entrada
)
