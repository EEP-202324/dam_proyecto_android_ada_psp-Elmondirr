import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appeventos.model.Evento
import com.example.appeventos.model.UserRol
import com.example.appeventos.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val Verdoso = Color(0xFF00695C)
val White = Color.White

@Composable
fun EventListScreen(navController: NavController) {
    val context = LocalContext.current
    var eventos by remember { mutableStateOf(listOf<Evento>()) }

    LaunchedEffect(Unit) {
        fetchEvents(context) { eventos = it }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(eventos) { evento ->
            EventItem(evento, navController)
        }
    }
}

fun fetchEvents(context: Context, updateEvents: (List<Evento>) -> Unit) {
    ApiClient.apiService.getEvents().enqueue(object : Callback<List<Evento>> {
        override fun onResponse(call: Call<List<Evento>>, response: Response<List<Evento>>) {
            if (response.isSuccessful) {
                updateEvents(response.body() ?: emptyList())
            } else {
                Toast.makeText(context, "Fallo al cargar los eventos", Toast.LENGTH_SHORT).show()
            }
        }
        override fun onFailure(call: Call<List<Evento>>, t: Throwable) {
            Toast.makeText(context, "Error de red", Toast.LENGTH_SHORT).show()
        }
    })
}

@Composable
fun EventItem(evento: Evento, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(evento.titulo, style = MaterialTheme.typography.titleMedium, color = Verdoso)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (UserRol.rol.isNotEmpty() && UserRol.rol == "ADMIN") {
                        navController.navigate("adminEventDetail/${evento.id}")
                    } else {
                        navController.navigate("eventDetail/${evento.id}") }
                    },
                colors = ButtonDefaults.buttonColors(containerColor = Verdoso)
            ) {
                Text("Ver Detalles", color = White)
            }
        }
    }
}
