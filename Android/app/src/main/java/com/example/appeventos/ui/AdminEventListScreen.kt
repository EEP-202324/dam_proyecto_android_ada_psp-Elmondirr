import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminEventListScreen(navController: NavController) {
    val context = LocalContext.current
    var eventos by remember { mutableStateOf(listOf<Evento>()) }
    val showCreateDialog = remember { mutableStateOf(false) }
    val newTitulo = remember { mutableStateOf("") }
    val newAforo = remember { mutableStateOf("") }
    val newFecha = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        fetchEvents(context) { eventos = it }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(eventos) { evento ->
                AdminEventItem(evento, navController)
            }
        }

        FloatingActionButton(
            onClick = { showCreateDialog.value = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp) // Añade un poco de espacio desde los bordes de la pantalla
                .background(color = Color(0xFFE0F7FA)), // Configura el color del fondo aquí
            contentColor = Color.White
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Crear Evento")
        }
    }

    if (showCreateDialog.value) {
        AlertDialog(
            onDismissRequest = { showCreateDialog.value = false },
            title = { Text("Crear Nuevo Evento") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newTitulo.value,
                        onValueChange = { newTitulo.value = it },
                        label = { Text("Título") }
                    )
                    OutlinedTextField(
                        value = newAforo.value,
                        onValueChange = { newAforo.value = it },
                        label = { Text("Aforo") }
                    )
                    OutlinedTextField(
                        value = newFecha.value,
                        onValueChange = { newFecha.value = it },
                        label = { Text("Fecha") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCreateDialog.value = false
                        createEvent(context, newTitulo.value, newAforo.value.toInt(), parseStringToLocalDate(newFecha.value), navController)
                    }
                ) {
                    Text("Añadir")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showCreateDialog.value = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

fun AdminfetchEvents(context: Context, updateEvents: (List<Evento>) -> Unit) {
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
fun AdminEventItem(evento: Evento, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(evento.titulo, style = MaterialTheme.typography.titleMedium, color = Color(0xFFE0F7FA))
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (UserRol.rol.isNotEmpty() && UserRol.rol == "ADMIN") {
                        navController.navigate("adminEventDetail/${evento.id}")
                    } else {
                        navController.navigate("eventDetail/${evento.id}")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0F7FA))
            ) {
                Text("Ver Detalles", color = Color.White)
            }
        }
    }
}

// Función para guardar el nuevo evento
// Función para guardar el nuevo evento
@RequiresApi(Build.VERSION_CODES.O)
fun createEvent(context: Context, titulo: String, aforo: Int, fechaString: String, navController: NavController) {
    val fecha = parseStringToLocalDate(fechaString) // Se parsea la fecha
    if (fecha != null) {
        val evento = Evento(titulo = titulo, aforo = aforo, fecha = fecha.toString())
        ApiClient.apiService.createEvent(evento).enqueue(object : Callback<Evento> {
            override fun onResponse(call: Call<Evento>, response: Response<Evento>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Evento creado exitosamente", Toast.LENGTH_LONG).show()
                    navController.navigateUp()
                } else {
                    Toast.makeText(context, "Fallo al crear el evento: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Evento>, t: Throwable) {
                Toast.makeText(context, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    } else {
        Toast.makeText(context, "La fecha proporcionada no es válida", Toast.LENGTH_LONG).show()
    }
}


/**
 * Convierte una cadena de texto que representa una fecha en un objeto LocalDate.
 * @param dateString La cadena de texto de la fecha en formato "yyyy-MM-dd".
 * @return Un objeto LocalDate correspondiente a la fecha de la cadena, o null si la cadena no es válida o es "null".
 */
@RequiresApi(Build.VERSION_CODES.O)
fun parseStringToLocalDate(dateString: String): String {
    return try {
        if (dateString != null && dateString != "null") {
            LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE).toString()
        } else {
            LocalDate.now().toString() // Retorna la fecha actual si la cadena es nula o "null"
        }
    } catch (e: DateTimeParseException) {
        LocalDate.now().toString()
    }
}
