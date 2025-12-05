package com.example.reclamos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reclamos.viewmodel.ReclamosViewModel

@Composable
fun ReclamosListScreen(
    viewModel: ReclamosViewModel,
    onAgregarClick: () -> Unit,
    onEditarClick: (Long) -> Unit,
    onVerDetalle: (Long) -> Unit,
    onLogout: () -> Unit
)
 {
     val lista by viewModel.listaReclamos.observeAsState(emptyList())

     if (lista.isEmpty()) {
         Box(
             modifier = Modifier.fillMaxSize(),
             contentAlignment = Alignment.Center
         ) {
             CircularProgressIndicator()
         }
         return
     }


     val loading by viewModel.loading.observeAsState(false)

    LaunchedEffect(Unit) { viewModel.cargarReclamos() }

    Column(Modifier.padding(16.dp)) {

        Button(onClick = onAgregarClick) {
            Text("Agregar Reclamo")
        }

        Button(
            onClick = onLogout,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Cerrar sesión")
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (loading) {
            CircularProgressIndicator()
            return@Column
        }

        LazyColumn {
            items(lista) { reclamo ->

                Column(Modifier.padding(12.dp)) {

                    Text("Nombre: ${reclamo.nombre}")
                    Text("Descripción: ${reclamo.descripcion}")
                    Text("Categoría: ${reclamo.categoria}")

                    Row {

                        Button(onClick = { onVerDetalle(reclamo.id) }) {
                            Text("Ver detalle")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = { onEditarClick(reclamo.id) }) {
                            Text("Editar")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = { viewModel.eliminarReclamo(reclamo.id) }) {
                            Text("Eliminar")
                        }
                    }

                    Divider(Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}
