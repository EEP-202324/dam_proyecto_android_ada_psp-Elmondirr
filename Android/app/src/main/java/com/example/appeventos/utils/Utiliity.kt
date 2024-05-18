package com.example.appeventos.utils

import android.content.Context

// Función para obtener el ID de usuario almacenado en las preferencias compartidas
fun getUserId(context: Context): Int {
    // Obtiene una instancia de SharedPreferences con el nombre "AppEventos" en modo privado
    val sharedPreferences = context.getSharedPreferences("AppEventos", Context.MODE_PRIVATE)

    // Retorna el valor asociado con la clave "userId", o -1 si no se encuentra la clave
    return sharedPreferences.getInt("userId", -1) // -1 como valor predeterminado si no se encuentra el ID
}
