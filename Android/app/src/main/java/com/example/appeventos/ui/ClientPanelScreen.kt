package com.example.appeventos.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ClientPanelScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF0F4F8) // Fondo claro pero neutral para un look más moderno y profesional
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp), // Aumentar el padding para una apariencia más espaciosa
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally // Centrar horizontalmente los elementos
        ) {
            Text(
                "Panel de Cliente",
                fontSize = 28.sp, // Aumentar el tamaño de la fuente para más impacto visual
                fontWeight = FontWeight.Bold, // Hacer el texto más prominente
                color = Color(0xFF00574B) // Un tono de verde más oscuro para más elegancia
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Botones con esquinas redondeadas para un diseño más amigable y moderno
            Button(
                onClick = { navController.navigate("profile") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)), // Un verde más vibrante para captar atención
                shape = RoundedCornerShape(12.dp), // Esquinas redondeadas
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Perfil", color = Color.White, fontSize = 18.sp) // Aumentar la fuente para mejor legibilidad
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("events") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buscar Eventos", color = Color.White, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("tickets") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mis Entradas", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}
