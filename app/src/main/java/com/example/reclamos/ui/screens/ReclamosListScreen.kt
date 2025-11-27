package com.example.reclamos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reclamos.model.Reclamo
import com.example.reclamos.viewmodel.ReclamosViewModel

@Composable
fun ReclamosListScreen(
    onAgregarClick: () -> Unit,
    onEditarClick: (Long) -> Unit,
    viewModel: ReclamosViewModel = viewModel()
) {
    val lista by viewModel.listaReclamos.observeAsState(emptyList())

    LaunchedEffect(Unit) { viewModel.cargarReclamos() }

    Column(Modifier.padding(16.dp)) {

        Button(onClick = onAgregarClick) {
            Text("Agregar Reclamo")
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn {
            items(lista) { reclamo ->
                ReclamoItem(
                    reclamo = reclamo,
                    onEditar = { onEditarClick(reclamo.id) },
                    onEliminar = { viewModel.eliminarReclamo(reclamo.id) }
                )
            }
        }
    }
}

@Composable
fun ReclamoItem(reclamo: Reclamo, onEditar: () -> Unit, onEliminar: () -> Unit) {
    Column(Modifier.padding(8.dp)) {
        Text("Nombre: ${reclamo.nombre}")
        Text("Descripción: ${reclamo.descripcion}")
        Text("Categoría: ${reclamo.categoria}")
        Button(onClick = onEditar) { Text("Editar") }
        Button(onClick = onEliminar) { Text("Eliminar") }
        Spacer(Modifier.height(12.dp))
    }
}
