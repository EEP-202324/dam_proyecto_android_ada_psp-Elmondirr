package com.example.appeventos.network

import com.example.appeventos.model.*
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    // Endpoint para iniciar sesión. Envía una solicitud POST con LoginRequest y recibe una respuesta de tipo AuthResponse
    @POST("usuarios/login")
    fun login(@Body loginRequest: LoginRequest): Call<AuthResponse>

    // Endpoint para registrarse. Envía una solicitud POST con RegisterRequest y recibe una respuesta de tipo AuthResponse
    @POST("usuarios/register")
    fun register(@Body registerRequest: RegisterRequest): Call<AuthResponse>

    // Endpoint para obtener todos los eventos. Envía una solicitud GET y recibe una lista de objetos Evento
    @GET("eventos")
    fun getEvents(): Call<List<Evento>>

    // Endpoint para obtener un evento específico por su ID. Envía una solicitud GET con un ID y recibe un objeto Evento
    @POST("eventos")
    fun createEvent(@Body event: Evento): Call<Evento>

    // Endpoint para obtener un evento específico por su ID. Envía una solicitud GET con un ID y recibe un objeto Evento
    @GET("eventos/{id}")
    fun getEvent(@Path("id") id: Int): Call<Evento>

    // Endpoint para suscribirse a un evento. Envía una solicitud POST con SuscribirRequest y recibe una respuesta de tipo SuscribirResponse
    @POST("eventos/suscribir")
    fun subscribeToEvent(@Body subscribeRequest: SuscribirRequest): Call<SuscribirResponse>

    // Endpoint para obtener todas las entradas. Envía una solicitud GET y recibe una lista de objetos Entrada
    @GET("entradas")
    fun getTickets(): Call<List<Entrada>>

    // Endpoint para obtener una entrada específica por su ID. Envía una solicitud GET con un ID y recibe un objeto Entrada
    @GET("entradas/{id}")
    fun getTicket(@Path("id") id: Int): Call<Entrada>

    // Endpoint para usar una entrada. Envía una solicitud POST con UsarEntradaRequest y recibe una respuesta de tipo UsarEntradaResponse
    @POST("entradas/usar")
    fun useTicket(@Body useTicketRequest: UsarEntradaRequest): Call<UsarEntradaResponse>

    // para crear ticket
    @POST("entradas")
    open fun createTicket(
        @Query("usuarioId") userId: Int,
        @Query("eventoId") eventId: Int,
        @Query("titulo") quantity: String
    ): Call<SuscribirResponse?>?

    // Endpoint para obtener el perfil de usuario. Envía una solicitud GET y recibe un objeto Usuario
    @GET("usuarios/perfil/{id}")
    fun getUserProfile(@Path ("id") userId: Int): Call<Usuario>

    // Endpoint para actualizar el perfil de usuario. Envía una solicitud PUT con el objeto Usuario actualizado y recibe el objeto Usuario actualizado
    @PUT("usuarios/perfil")
    fun updateUserProfile(@Body userProfile: Usuario): Call<Usuario>

    // método para eliminar un evento por ID
    @DELETE("eventos/{id}")
    fun deleteEvent(@Path("id") eventId: Int): Call<Void>

    // metodo para actualizar un evento por ID
    @PUT("eventos/{id}")
    fun updateEvent(@Path("id") eventId: Int, @Body event: Evento): Call<Evento>

    @GET("tickets")
    fun getTickets(@Query("page") page: Int): Call<List<Entrada>>

    // Endpoint para eliminar una entrada específica por su ID
    @DELETE("entradas/{id}")
    fun deleteTicket(@Path("id") id: Int): Call<Void>
}
