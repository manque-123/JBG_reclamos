package com.example.reclamos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reclamos.viewmodel.ReclamosViewModel

@Composable
fun ReclamoEditarScreen(
    id: Long,
    onBack: () -> Unit,
    viewModel: ReclamosViewModel = viewModel()
) {
    val lista = viewModel.listaReclamos.value ?: emptyList()
    val reclamo = lista.find { it.id == id }

    var nombre by remember { mutableStateOf(reclamo?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(reclamo?.descripcion ?: "") }
    var categoria by remember { mutableStateOf(reclamo?.categoria ?: "") }

    Column(Modifier.padding(16.dp)) {

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
        OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") })

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            if (reclamo != null) {
                val act = reclamo.copy(
                    nombre = nombre,
                    descripcion = descripcion,
                    categoria = categoria
                )

                viewModel.actualizarReclamo(id, act) {
                    onBack()
                }
            }

        }) { Text("Guardar Cambios") }
    }
}
