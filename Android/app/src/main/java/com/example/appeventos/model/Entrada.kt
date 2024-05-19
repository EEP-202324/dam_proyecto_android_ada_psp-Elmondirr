package com.example.appeventos.model

// Definición de la clase de datos Entrada
data class Entrada(
    val id: Int,        // Identificador único de la entrada
    val uuid: String,   // UUID único de la entrada
    val usuario: Usuario, // Usuario asociado a la entrada
    val evento: Evento,   // Evento asociado a la entrada
    val titulo: String? // Título del evento
)
