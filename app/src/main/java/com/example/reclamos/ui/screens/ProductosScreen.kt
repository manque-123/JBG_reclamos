package com.example.reclamos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reclamos.viewmodel.ProductosViewModel

@Composable
fun ProductosScreen(viewModel: ProductosViewModel = viewModel()) {

    val lista by viewModel.productos.observeAsState(emptyList())
    val mensaje by viewModel.mensaje.observeAsState("")

    LaunchedEffect(Unit) {
        viewModel.cargarProductos()
    }

    Column(Modifier.padding(16.dp)) {

        Text(
            text = "Listado de Productos",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        if (mensaje.isNotEmpty()) {
            Text(text = mensaje, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(lista) { producto ->
                Text("${producto.nombre} - $${producto.precio}")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
