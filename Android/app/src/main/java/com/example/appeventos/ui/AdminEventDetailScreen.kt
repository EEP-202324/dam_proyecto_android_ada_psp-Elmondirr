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

    // Cargar el evento cuando el composable se compone y cada vez que cambia el eventoId
    LaunchedEffect(eventoId) {
        getEvent(context, eventoId, eventoState)
    }

    if (showConfirmationDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showConfirmationDialog.value = false
            },
            title = {
                Text("Confirmación de Borrado")
            },
            text = {
                Text("¿Estás seguro de que deseas borrar este evento?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog.value = false
                        deleteEvent(context, eventoId, navController)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00695C))
                ) {
                    Text("Confirmar", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showConfirmationDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)) // Ajuste del color del botón 'Cancelar' a rojo
                ) {
                    Text("Cancelar", color = Color.White)
                }
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF7F9FC) // Un fondo suave y claro
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                Divider(color = Color(0xFF00695C), thickness = 2.dp)
                Text(
                    "Detalles del Evento",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Divider(color = Color(0xFF00695C), thickness = 2.dp, modifier = Modifier.padding(vertical = 4.dp))
                eventoState.value?.let { evento ->
                    // Mostrar los detalles del evento
                    Text("Título: ${evento.titulo}", fontSize = 18.sp, color = Color.DarkGray)
                    Text("Aforo: ${evento.aforo}", fontSize = 18.sp, color = Color.DarkGray)
                    Text("Fecha: ${evento.fecha}", fontSize = 18.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(20.dp))
                } ?: Text("Cargando detalles del evento...", fontSize = 16.sp, color = Color.Gray)

                // Botones para modificar y borrar
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            eventoState.value?.let { evento ->
                                navController.navigate("editEvento/${evento.id}")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Modificar", color = Color.White, fontSize = 16.sp)
                    }
                    Button(
                        onClick = {
                            showConfirmationDialog.value = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)) // Ajuste del color del botón 'Borrar' a rojo
                    ) {
                        Text("Borrar", color = Color.White, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

// Función para cargar el evento desde la API
fun getEvent(context: Context, eventId: Int, eventoState: MutableState<Evento?>) {
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
