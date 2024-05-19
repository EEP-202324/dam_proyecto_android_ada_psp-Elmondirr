package com.example.appeventos.ui

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appeventos.R
import com.example.appeventos.model.UsarEntradaRequest
import com.example.appeventos.model.UsarEntradaResponse
import com.example.appeventos.network.ApiClient
import com.example.appeventos.utils.getUserId
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var verdoso = Color(0xFF4CAF50)

@Composable
fun AdminTicketDetailScreen(navController: NavController, entradaId: Int, ticketUuid: String) {
    val context = LocalContext.current
    val qrCodeBitmap = remember { generateQRCode(ticketUuid) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF0F4F8)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ticket), // Suponiendo que tienes una imagen genérica de eventos
                contentDescription = "Imagen del Evento",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Detalles de la Entrada",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Verdoso,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            qrCodeBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "QR Code",
                    modifier = Modifier
                        .size(200.dp)
                        .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
                        .padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val userId = getUserId(context)
                    if (userId != -1) {
                        val usarEntradaRequest = UsarEntradaRequest(entradaId = entradaId, usuarioId = userId)
                        ApiClient.apiService.useTicket(usarEntradaRequest).enqueue(object : Callback<UsarEntradaResponse> {
                            override fun onResponse(call: Call<UsarEntradaResponse>, response: Response<UsarEntradaResponse>) {
                                if (response.isSuccessful && response.body()?.exito == true) {
                                    Toast.makeText(context, "Entrada utilizada correctamente", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Fallo al usar la entrada", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onFailure(call: Call<UsarEntradaResponse>, t: Throwable) {
                                Toast.makeText(context, "Error de red", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } else {
                        Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Verdoso),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Usar Entrada", color = MaterialTheme.colorScheme.onPrimary, fontSize = 18.sp)
            }
        }
    }
}

// Función para generar un código QR a partir de un texto
fun AdmingenerateQRCode(text: String): Bitmap? {
    val writer = QRCodeWriter()
    return try {
        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bitmap
    } catch (e: Exception) {
        null
    }
}
