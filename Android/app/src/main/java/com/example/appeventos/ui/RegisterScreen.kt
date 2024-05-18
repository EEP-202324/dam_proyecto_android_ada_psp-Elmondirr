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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appeventos.R
import com.example.appeventos.model.RegisterRequest
import com.example.appeventos.model.AuthResponse
import com.example.appeventos.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun RegisterScreen(navController: NavController) {
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFE0F7FA)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.reg),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Bienvenido al registro",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004D40)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Introduzca sus datos para registrarse", color = Color(0xFF004D40))

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth().background(Color.White),
                keyboardOptions = KeyboardOptions.Default
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth().background(Color.White),
                keyboardOptions = KeyboardOptions.Default
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth().background(Color.White),
                keyboardOptions = KeyboardOptions.Default
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().background(Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Validar que no estén vacíos y no sean sólo espacios en blanco
                    if (userName.isBlank() || email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Por favor, complete todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                    } else {
                        val registerRequest = RegisterRequest(userName.trim(), password, email.trim(), apellidos.trim())
                        Log.d("RegisterScreen", "Sending register request: $registerRequest")
                        ApiClient.apiService.register(registerRequest).enqueue(object : Callback<AuthResponse> {
                            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                                handleRegisterResponse(response, context, navController)
                            }
                            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                                Log.e("RegisterScreen", "Register failed: ${t.message}", t)
                                Toast.makeText(context, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004D40)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¿Ya tienes cuenta? Inicia sesión",
                modifier = Modifier.clickable {
                    navController.navigate("login")
                },
                color = Color(0xFF00796B)
            )
        }
    }
}

private fun handleRegisterResponse(response: Response<AuthResponse>, context: Context, navController: NavController) {
    if (response.isSuccessful) {
        Log.d("RegisterScreen", "Register successful")
        navController.navigate("login")
    } else {
        Log.e("RegisterScreen", "Register failed with response code: ${response.code()}")
        Log.e("RegisterScreen", "Response error body: ${response.errorBody()?.string()}")
        Toast.makeText(context, "Fallo al registrar: ${response.message()}", Toast.LENGTH_SHORT).show()
    }
}
