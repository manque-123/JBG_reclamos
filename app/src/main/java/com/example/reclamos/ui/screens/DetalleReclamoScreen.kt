package com.example.reclamos.ui.screens
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reclamos.viewmodel.ReclamosViewModel



@Composable
fun DetalleReclamoScreen(
    id: Long,
    onBack: () -> Unit,
    viewModel: ReclamosViewModel = viewModel()
) {
    val lista by viewModel.listaReclamos.observeAsState(emptyList())

    val reclamo = lista.find { it.id == id }

    Column(Modifier.padding(16.dp)) {

        Button(onClick = onBack) {
            Text("Volver")
        }

        Text("Detalle del Reclamo", style = MaterialTheme.typography.titleLarge)

        reclamo?.let {
            Text("Nombre: ${it.nombre}")
            Text("Descripción: ${it.descripcion}")
            Text("Categoría: ${it.categoria}")
            Text("Email: ${it.email}")
            Text("Latitud: ${it.latitud}")
            Text("Longitud: ${it.longitud}")
        } ?: Text("Reclamo no encontrado")
    }
}
