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
    var page by remember { mutableStateOf(1) } // Estado para la página actual

    // LaunchedEffect para llamar a la API una sola vez al inicializar el composable
    LaunchedEffect(key1 = true) {
        fetchTickets(context, page) { loadedTickets ->
            entradas = loadedTickets
        }
    }

    // UI para mostrar las entradas
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(entradas) { entrada ->
                TicketItem(entrada, navController)
                Spacer(modifier = Modifier.height(32.dp)) // Espacio entre los elementos
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para cargar más entradas
        Button(
            onClick = {
                page += 1 // Incrementa la página
                fetchTickets(context, page) { loadedTickets ->
                    entradas = entradas + loadedTickets // Añade las nuevas entradas a las existentes
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cargar más entradas")
        }
    }
}

// Función para llamar a la API y obtener las entradas
fun fetchTickets(context: Context, page: Int, updateTickets: (List<Entrada>) -> Unit) {
    ApiClient.apiService.getTickets(page).enqueue(object : Callback<List<Entrada>> {
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
    val tituloEvento = entrada.tituloEvento ?: "Título no disponible" // Manejar nulos
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text("Entrada para evento: $tituloEvento", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { navController.navigate("ticketDetail/${entrada.id}/${entrada.uuid}") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Abrir tu entrada")
        }
    }
}
