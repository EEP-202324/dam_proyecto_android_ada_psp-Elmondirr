package com.example.appeventos.model

// Definición de la clase de datos AuthResponse
data class AuthResponse(
    val usuario: Usuario, // Usuario asociado a la respuesta de autenticación
    val token: String?    // Token opcional devuelto después de la autenticación
)
