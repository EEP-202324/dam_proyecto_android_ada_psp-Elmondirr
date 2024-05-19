import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import com.example.appeventos.model.User
import com.example.appeventos.model.Usuario
import com.example.appeventos.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    var nombreUsuario by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        ApiClient.apiService.getUserProfile(User.id).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        nombreUsuario = it.nombre
                        correo = it.email
                        apellidos = it.apellidos
                        rol = it.rol
                    }
                } else {
                    Toast.makeText(context, "Fallo al cargar el perfil", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Toast.makeText(context, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFE0F7FA)  // Fondo verdoso claro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.perfil),
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text("Perfil", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color(0xFF004D40))
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = nombreUsuario,
                onValueChange = { nombreUsuario = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellidos") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Corregido para comparación insensible a mayúsculas y minúsculas
            OutlinedTextField(
                value = if (rol.equals("admin", ignoreCase = true)) "ADMIN" else "CLIENTE",                onValueChange = {},
                readOnly = true,
                label = { Text("Rol") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // Recupera el ID del usuario de una fuente segura (e.g., almacenamiento seguro)
                    val userId = User.id

                    val userProfile = Usuario(id = userId, nombre = nombreUsuario, email = correo, contrasena = "", rol = rol, apellidos = apellidos)

                    if (userId != 0) {
                        ApiClient.apiService.updateUserProfile(userProfile).enqueue(object : Callback<Usuario> {
                            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Fallo al actualizar el perfil: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                                }
                            }

                            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                                Toast.makeText(context, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
                            }
                        })
                    } else {
                        Toast.makeText(context, "ID de usuario no válido", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004D40))
            ) {
                Text("Guardar Cambios", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    deleteProfile(context, navController, User.id)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Text("Borrar Perfil", color = Color.White)
            }
        }
    }
}

fun deleteProfile(context: Context, navController: NavController, userId: Int) {
    ApiClient.apiService.deleteUser(userId).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                Toast.makeText(context, "Perfil eliminado", Toast.LENGTH_SHORT).show()
                navController.navigate("login") // Redirigir a la pantalla de inicio de sesión
            } else {
                Toast.makeText(context, "Fallo al eliminar el perfil: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Toast.makeText(context, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}
