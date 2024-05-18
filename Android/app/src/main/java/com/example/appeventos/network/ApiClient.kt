package com.example.appeventos.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // Constante que define la URL base para las solicitudes de la API
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Objeto Retrofit configurado con la URL base y el convertidor Json
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL) // Configura la URL base para las solicitudes de la API
        .addConverterFactory(GsonConverterFactory.create()) // Añade un convertidor para deserializar JSON
        .build() // Construye el objeto Retrofit

    // Instancia del servicio API creada a partir de la interfaz ApiService
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
