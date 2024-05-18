package com.example.appeventos.model

import java.io.Serializable

// Definición de la clase de datos Evento
data class Evento(
    val id: Int,       // Identificador único del evento
    val titulo: String, // Título del evento
    val fecha: String, // Fecha del evento
    val aforo: Int     // Capacidad máxima del evento
): Serializable
