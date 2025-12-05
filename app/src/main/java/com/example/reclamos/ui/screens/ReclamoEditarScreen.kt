package com.example.reclamos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reclamos.model.Reclamo
import com.example.reclamos.viewmodel.ReclamosViewModel

@Composable
fun ReclamoEditarScreen(
    id: Long,
    navController: NavController,
    reclamosVM: ReclamosViewModel,
    onBack: () -> Unit
) {
    val lista by reclamosVM.listaReclamos.observeAsState(emptyList())
    val reclamo = lista.find { it.id == id }

    var nombre by remember { mutableStateOf(reclamo?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(reclamo?.descripcion ?: "") }
    var categoria by remember { mutableStateOf(reclamo?.categoria ?: "") }

    Column(Modifier.padding(16.dp)) {

        Text("Editar Reclamo", style = MaterialTheme.typography.titleLarge)

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
            val editado = Reclamo(
                id = id,
                nombre = nombre,
                descripcion = descripcion,
                categoria = categoria,
                email = reclamo?.email ?: "",
                latitud = reclamo?.latitud ?: 0.0,
                longitud = reclamo?.longitud ?: 0.0
            )
            reclamosVM.editarReclamo(id, editado)
            onBack()
        }) {
            Text("Guardar Cambios")
        }
    }
}
