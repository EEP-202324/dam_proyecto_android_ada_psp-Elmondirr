package com.example.appeventos.model

// Definición de la clase de datos Usuario
data class Usuario(
    val id: Int, // Identificador único del usuario
    val nombre: String, // Nombre del usuario
    val apellidos: String, // Apellidos del usuario
    val email: String, // Correo electrónico del usuario
    val contrasena: String, // Contraseña del usuario
    val rol: String // Rol del usuario, "ADMIN" o "CLIENTE"
)
