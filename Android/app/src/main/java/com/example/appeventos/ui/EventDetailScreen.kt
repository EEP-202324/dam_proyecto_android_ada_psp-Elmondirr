package com.example.appeventos.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appeventos.R
import com.example.appeventos.model.Evento
import com.example.appeventos.model.SuscribirRequest
import com.example.appeventos.model.SuscribirResponse
import com.example.appeventos.model.User
import com.example.appeventos.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(navController: NavController, eventoId: Int) {
    var usuarioId = remember { mutableStateOf(User.id) }  // Asignar ID de usuario real
    val eventoState = remember { mutableStateOf<Evento?>(null) }
    val context = LocalContext.current

    // Cargar el evento cuando el composable se compone y cada vez que cambia el eventoId
    LaunchedEffect(eventoId) {
        getEvent(context, eventoId, eventoState)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) {
        it.calculateTopPadding() // Calcular el padding superior

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFFAFAFA)) // Gris claro
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.events),
                    contentDescription = "Imagen del Evento",
                    modifier = Modifier.size(150.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Detalles del Evento",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Divider(
                    color = Color(0xFFBDBDBD),
                    thickness = 2.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                eventoState.value?.let { evento ->
                    Text("Título: ${evento.titulo}", fontSize = 18.sp, color = Color(0xFF616161))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Aforo: ${evento.aforo}", fontSize = 18.sp, color = Color(0xFF616161))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Fecha: ${evento.fecha}", fontSize = 18.sp, color = Color(0xFF616161))
                } ?: Text("Cargando detalles del evento...", fontSize = 16.sp, color = Color.Gray)
                Button(
                    onClick = {
                        ApiClient.apiService.createTicket(usuarioId.value, eventId = eventoId, eventoState.value?.titulo
                            ?:"")
                            ?.enqueue(object : Callback<SuscribirResponse?> {
                                override fun onResponse(
                                    call: Call<SuscribirResponse?>,
                                    response: Response<SuscribirResponse?>
                                ) {
                                    if (response.isSuccessful && response.code() == 200) {
                                        Toast.makeText(
                                            context,
                                            "Compra exitosa",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Fallo al comprar",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onFailure(call: Call<SuscribirResponse?>, t: Throwable) {
                                    Toast.makeText(context, "Error de red", Toast.LENGTH_SHORT).show()
                                }
                            })
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00695C)),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Comprar entrada", color = Color.White, fontSize = 16.sp)
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
                Toast.makeText(
                    context,
                    "Fallo al cargar el evento: ${response.errorBody()?.string()}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        override fun onFailure(call: Call<Evento>, t: Throwable) {
            Toast.makeText(context, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}
