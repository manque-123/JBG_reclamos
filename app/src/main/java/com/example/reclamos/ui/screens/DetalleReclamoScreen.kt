package com.example.reclamos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.reclamos.viewmodel.ReclamosViewModel

@OptIn(ExperimentalMaterial3Api::class)@Composable
fun DetalleReclamoScreen(
    id: Long,
    viewModel: ReclamosViewModel,
    onBack: () -> Unit
)
{

    val lista by viewModel.listaReclamos.observeAsState(emptyList())
    val reclamo = lista.find { it.id == id }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Button(onClick = onBack) {
            Text("Volver")
        }

        Text("Detalle del Reclamo", style = MaterialTheme.typography.titleLarge)

        if (reclamo != null) {

            Text("Nombre: ${reclamo.nombre}")
            Text("Descripción: ${reclamo.descripcion}")
            Text("Categoría: ${reclamo.categoria}")
            Text("Email: ${reclamo.email}")

            Spacer(Modifier.height(8.dp))

            reclamo.fotoUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            Text("Latitud: ${reclamo.latitud}")
            Text("Longitud: ${reclamo.longitud}")

        } else {
            Text("Reclamo no encontrado", color = MaterialTheme.colorScheme.error)
        }
    }
}
