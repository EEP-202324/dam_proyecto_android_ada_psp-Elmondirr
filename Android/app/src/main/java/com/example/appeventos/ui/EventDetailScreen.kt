package com.example.appeventos.ui

import android.widget.Toast
import androidx.compose.foundation.Image
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
import com.example.appeventos.model.SuscribirRequest
import com.example.appeventos.model.SuscribirResponse
import com.example.appeventos.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun EventDetailScreen(navController: NavController, eventoId: Int) {
    var usuarioId = remember { mutableStateOf(0) }  // Asignar ID de usuario real
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        usuarioId.value = 1  // Obtener ID del usuario real
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF7F9FC)  // Un fondo suave y claro
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

            Button(
                onClick = {
                    val suscribirRequest = SuscribirRequest(eventoId = eventoId, usuarioId = usuarioId.value)
                    ApiClient.apiService.subscribeToEvent(suscribirRequest).enqueue(object : Callback<SuscribirResponse> {
                        override fun onResponse(call: Call<SuscribirResponse>, response: Response<SuscribirResponse>) {
                            if (response.isSuccessful && response.body()?.exito == true) {
                                Toast.makeText(context, "Suscripción exitosa", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Fallo al suscribirse", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<SuscribirResponse>, t: Throwable) {
                            Toast.makeText(context, "Error de red", Toast.LENGTH_SHORT).show()
                        }
                    })
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00695C)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Suscribirse", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}
