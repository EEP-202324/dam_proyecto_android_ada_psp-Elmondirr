import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appeventos.model.Entrada
import com.example.appeventos.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun TicketListScreen(navController: NavController) {
    val context = LocalContext.current
    var entradas by remember { mutableStateOf(listOf<Entrada>()) }

    // LaunchedEffect para llamar a la API una sola vez al inicializar el composable
    LaunchedEffect(key1 = true) {
        fetchTickets(context) { loadedTickets ->
            entradas = loadedTickets
        }
    }

    // UI para mostrar las entradas
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(entradas) { entrada ->
            TicketItem(entrada, navController)
        }
    }
}

// Función para llamar a la API y obtener las entradas
fun fetchTickets(context: Context, updateTickets: (List<Entrada>) -> Unit) {
    ApiClient.apiService.getTickets().enqueue(object : Callback<List<Entrada>> {
        override fun onResponse(call: Call<List<Entrada>>, response: Response<List<Entrada>>) {
            if (response.isSuccessful) {
                updateTickets(response.body() ?: emptyList())
            } else {
                Toast.makeText(context, "Fallo al cargar las entradas", Toast.LENGTH_SHORT).show()
            }
        }
        override fun onFailure(call: Call<List<Entrada>>, t: Throwable) {
            Toast.makeText(context, "Error de red", Toast.LENGTH_SHORT).show()
        }
    })
}

// Composable para representar cada entrada
@Composable
fun TicketItem(entrada: Entrada, navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text("Entrada para evento: ${entrada.evento.titulo}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { navController.navigate("ticketDetail/${entrada.id}/${entrada.uuid}") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver Detalles")
        }
    }
}
