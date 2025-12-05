package com.example.reclamos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.reclamos.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    loginVM: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val loading by loginVM.loading.observeAsState(false)
    val mensajeError by loginVM.mensajeError.observeAsState()

    var correo by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {

        Text("Correo:")
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Contraseña:")
        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                loginVM.login(correo, pass, context)
                if (loginVM.mensajeError.value == null) {
                    onLoginSuccess()
                }
            },
            enabled = !loading
        ) {
            Text("Ingresar")
        }

        if (loading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }

        mensajeError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
