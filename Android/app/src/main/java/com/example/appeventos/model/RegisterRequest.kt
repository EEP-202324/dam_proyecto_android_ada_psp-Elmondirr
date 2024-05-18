package com.example.appeventos.model

import com.google.gson.annotations.SerializedName

// Definición de la clase de datos RegisterRequest
data class RegisterRequest(
    @SerializedName("nombreUsuario")
    val nombre: String, // Nombre del usuario
    val contrasena: String, // Contraseña del usuario
    val email: String, // Correo electrónico del usuario
    val apellidos: String?// Apellidos del usuario
)
