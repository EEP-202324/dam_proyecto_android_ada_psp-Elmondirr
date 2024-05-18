package com.example.appeventos

import AdminEventListScreen
import EventListScreen
import ProfileScreen
import TicketListScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appeventos.ui.*
import com.example.appeventos.ui.AdminEventDetailScreen

class MainActivity : ComponentActivity() {
    // Método que se ejecuta cuando se crea la actividad
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //configuracion basica de la actividad
        // Configura el contenido de la actividad utilizando Compose
        setContent {
            EventApp() //configura el contenido de la actividad utilizando jetpack compose
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun EventApp() {
    // Crear un controlador de navegación
    val navController = rememberNavController()
    // Definir el host de navegación y las rutas
    NavHost(navController = navController, startDestination = "login") {
        // Composable para la pantalla de inicio de sesión
        composable("login") { LoginScreen(navController) }
        // Composable para la pantalla de registro
        composable("register") { RegisterScreen(navController) }
        // Composable para la pantalla del panel de administrador
        composable("admin") { AdminPanelScreen(navController) }
        // Composable para la pantalla del panel de cliente
        composable("client") { ClientPanelScreen(navController) }
        // Composable para la pantalla de detalles del evento
        composable("eventDetail/{eventoId}") { backStackEntry ->
            backStackEntry.arguments?.getString("eventoId")?.toIntOrNull()?.let { eventoId ->
                EventDetailScreen(navController, eventoId)
            } ?: throw IllegalArgumentException("Evento ID is required")
        }
        composable("adminEventDetail/{eventoId}") { backStackEntry ->
            backStackEntry.arguments?.getString("eventoId")?.let { eventoId ->
                AdminEventDetailScreen(navController, eventoId.toInt())
            } ?: throw IllegalArgumentException("Admin Evento ID is required")
        }
        // Composable para la pantalla de detalles del ticket
        composable("ticketDetail/{entradaId}/{ticketUuid}") { backStackEntry ->
            val entradaId = backStackEntry.arguments?.getString("entradaId")?.toInt() ?: 0
            val ticketUuid = backStackEntry.arguments?.getString("ticketUuid") ?: ""
            TicketDetailScreen(navController, entradaId, ticketUuid)
        }
        // Composable para la pantalla de lista de eventos
        composable("events") { EventListScreen(navController) }
        // Composable para la pantalla de lista de eventos
        composable("adminEvents") { AdminEventListScreen(navController) }
        // Composable para la pantalla de lista de tickets
        composable("tickets") { TicketListScreen(navController) }
        // Composable para la pantalla de perfil del usuario
        composable("profile") { ProfileScreen(navController) }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    // Función para mostrar una vista previa de la aplicación en el editor
    EventApp()
}
