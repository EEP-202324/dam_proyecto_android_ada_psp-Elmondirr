package com.example.appeventos.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appeventos.model.Evento
import com.example.appeventos.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun AdminEventDetailScreen(navController: NavController, eventoId: Int) {
    val context = LocalContext.current
    val eventoState = remember { mutableStateOf<Evento?>(null) }
    val showConfirmationDialog = remember { mutableStateOf(false) }
    val editMode = remember { mutableStateOf(false) }

    // Form states
    val titulo = remember { mutableStateOf("") }
    val aforo = remember { mutableStateOf("") }
    val fecha = remember { mutableStateOf("") }

    // Cargar el evento cuando el composable se compone y cada vez que cambia el eventoId
    LaunchedEffect(eventoId) {
        AdmingetEvent(context, eventoId, eventoState)
    }

    // Update form fields when eventoState changes
    LaunchedEffect(eventoState.value) {
        eventoState.value?.let { evento ->
            titulo.value = evento.titulo
            aforo.value = evento.aforo.toString()
            fecha.value = evento.fecha
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFAFAFA) // Un fondo más claro y suave
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Divider(color = Color(0xFFBDBDBD), thickness = 2.dp)
            Text(
                "Detalles del Evento",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF424242)
            )
            Divider(color = Color(0xFFBDBDBD), thickness = 2.dp, modifier = Modifier.padding(vertical = 4.dp))
            eventoState.value?.let { evento ->
                if (editMode.value) {
                    // Mostrar el formulario de edición
                    OutlinedTextField(
                        value = titulo.value,
                        onValueChange = { titulo.value = it },
                        label = { Text("Título") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    OutlinedTextField(
                        value = aforo.value,
                        onValueChange = { aforo.value = it },
                        label = { Text("Aforo") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    OutlinedTextField(
                        value = fecha.value,
                        onValueChange = { fecha.value = it },
                        label = { Text("Fecha") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                editMode.value = false
                                updateEvent(context, eventoId, titulo.value, aforo.value.toInt(), fecha.value, navController)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784)) // Verde pastel
                        ) {
                            Text("Guardar", color = Color.White, fontSize = 16.sp)
                        }
                        Button(
                            onClick = {
                                editMode.value = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)) // Rojo pastel
                        ) {
                            Text("Cancelar", color = Color.White, fontSize = 16.sp)
                        }
                    }
                } else {
                    // Mostrar los detalles del evento
                    Text("Título: ${evento.titulo}", fontSize = 18.sp, color = Color(0xFF616161))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Aforo: ${evento.aforo}", fontSize = 18.sp, color = Color(0xFF616161))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Fecha: ${evento.fecha}", fontSize = 18.sp, color = Color(0xFF616161))
                    Spacer(modifier = Modifier.height(20.dp))

                    // Botones para modificar y borrar
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                editMode.value = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784)) // Verde pastel
                        ) {
                            Text("Modificar", color = Color.White, fontSize = 16.sp)
                        }
                        Button(
                            onClick = {
                                showConfirmationDialog.value = true
                                deleteEvent(context, eventoId, navController)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)) // Rojo pastel
                        ) {
                            Text("Borrar", color = Color.White, fontSize = 16.sp)
                        }
                    }
                }
            } ?: Text("Cargando detalles del evento...", fontSize = 16.sp, color = Color.Gray)
        }
    }
}

// Función para cargar el evento desde la API
fun AdmingetEvent(context: Context, eventId: Int, eventoState: MutableState<Evento?>) {
    ApiClient.apiService.getEvent(eventId).enqueue(object : Callback<Evento> {
        override fun onResponse(call: Call<Evento>, response: Response<Evento>) {
            if (response.isSuccessful) {
                eventoState.value = response.body()
            } else {
                Toast.makeText(context, "Fallo al cargar el evento: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<Evento>, t: Throwable) {
            Toast.makeText(context, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

// Función para actualizar el evento
fun updateEvent(context: Context, eventId: Int, titulo: String, aforo: Int, fecha: String, navController: NavController) {
    val updatedEvent = Evento(id = eventId, titulo = titulo, aforo = aforo, fecha = fecha)
    ApiClient.apiService.updateEvent(eventId, updatedEvent).enqueue(object : Callback<Evento> {
        override fun onResponse(call: Call<Evento>, response: Response<Evento>) {
            if (response.isSuccessful) {
                Toast.makeText(context, "Evento actualizado exitosamente", Toast.LENGTH_LONG).show()
                navController.navigateUp()
            } else {
                Toast.makeText(context, "Error al actualizar el evento: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<Evento>, t: Throwable) {
            Toast.makeText(context, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

// Función para borrar un evento
fun deleteEvent(context: Context, eventId: Int, navController: NavController) {
    ApiClient.apiService.deleteEvent(eventId).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                Toast.makeText(context, "Evento borrado exitosamente", Toast.LENGTH_LONG).show()
                navController.navigateUp()
            } else {
                Toast.makeText(context, "Error al borrar el evento: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Toast.makeText(context, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}
