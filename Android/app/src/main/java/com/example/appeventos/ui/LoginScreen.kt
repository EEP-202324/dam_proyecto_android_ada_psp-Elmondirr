package com.example.appeventos.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appeventos.R
import com.example.appeventos.model.LoginRequest
import com.example.appeventos.model.AuthResponse
import com.example.appeventos.model.UserRol
import com.example.appeventos.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFE0F7FA) // Fondo verdoso claro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente los elementos
        ) {
            Image(
                painter = painterResource(id = R.drawable.log),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )

            Text(
                text = "Hola, bienvenidos!",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004D40)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Introduzca sus datos", color = Color(0xFF004D40))

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White), // Fondo blanco para el campo de texto
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White) // Fondo blanco para el campo de texto
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val loginRequest = LoginRequest(email, contrasena)
                    Log.d("LoginScreen", "Sending login request: $loginRequest")
                    ApiClient.apiService.login(loginRequest).enqueue(object : Callback<AuthResponse> {
                        override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                            if (response.isSuccessful) {
                                val authResponse = response.body()
                                if (authResponse != null) {
                                    Log.d("LoginScreen", "Login successful: $authResponse")
                                    saveUserId(context, authResponse.usuario.id)
                                    val isAdmin = authResponse.usuario.rol == "ADMIN"
                                    UserRol.rol = authResponse.usuario.rol
                                    if (isAdmin) {
                                        navController.navigate("admin")
                                    } else {
                                        navController.navigate("client")
                                    }
                                } else {
                                    Log.e("LoginScreen", "AuthResponse is null")
                                    Toast.makeText(context, "Fallo al iniciar sesión", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Log.e("LoginScreen", "Login failed with response code: ${response.code()}")
                                Toast.makeText(context, "Fallo al iniciar sesión", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                            Log.e("LoginScreen", "Login failed: ${t.message}", t)
                            Toast.makeText(context, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004D40)), // Color del botón verde oscuro
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión", color = Color.White, fontSize = 16.sp) // Texto blanco en el botón
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No tienes cuenta? Regístrate",
                modifier = Modifier.clickable {
                    navController.navigate("register")
                },
                color = Color(0xFF00796B)
            )
        }
    }
}

fun saveUserId(context: Context, userId: Int) {
    Log.i("LoginScreen", "Saving user ID: $userId")
    val sharedPreferences = context.getSharedPreferences("AppEventos", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putInt("userId", userId)
        apply()
    }
}
