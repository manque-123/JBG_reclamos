package com.example.reclamos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reclamos.model.Reclamo
import com.example.reclamos.viewmodel.ReclamosViewModel

@Composable
fun ReclamoCrearScreen(
    onBack: () -> Unit,
    viewModel: ReclamosViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
        OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            val nuevo = Reclamo(
                id = 0,
                nombre = nombre,
                descripcion = descripcion,
                categoria = categoria,
                email = email,
                telefono = null,
                nroCompra = null,
                sucursal = null,
                fotoUri = null,
                latitud = null,
                longitud = null
            )

            viewModel.crearReclamo(nuevo) {
                onBack()
            }

        }) { Text("Crear Reclamo") }
    }
}
