package com.example.reclamos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reclamos.model.Reclamo
import com.example.reclamos.viewmodel.ReclamosViewModel

@Composable
fun ReclamoCrearScreen(
    navController: NavController,
    reclamosVM: ReclamosViewModel,
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {

        Text("Crear Reclamo", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") }
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") }
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoría") }
        )

        Spacer(Modifier.height(20.dp))

        Button(onClick = {
            val nuevo = Reclamo(
                id = 0,
                nombre = nombre,
                descripcion = descripcion,
                categoria = categoria,
                email = "",
                latitud = 0.0,
                longitud = 0.0
            )
            reclamosVM.crearReclamo(nuevo)
            onBack()
        }) {
            Text("Crear")
        }
    }
}
