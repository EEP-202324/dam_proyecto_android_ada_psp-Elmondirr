package com.example.appeventos.model

// Definición de la clase de datos SuscribirRequest
data class SuscribirRequest(
    val eventoId: Int, // ID del evento al que el usuario desea suscribirse
    val usuarioId: Int // ID del usuario que desea suscribirse al evento
)
