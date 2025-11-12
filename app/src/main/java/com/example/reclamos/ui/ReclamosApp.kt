package com.example.reclamos.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.reclamos.model.Reclamo
import com.example.reclamos.viewmodel.ReclamosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReclamosApp(vm: ReclamosViewModel) {
    val reclamos by vm.reclamos.observeAsState(emptyList())
    val mensaje by vm.mensaje.observeAsState()
    val fotoUri by vm.fotoUri.observeAsState()
    val latLong by vm.latLong.observeAsState()

    LaunchedEffect(Unit) { vm.cargar() }

    var pantalla by remember { mutableStateOf("lista") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("JBG_reclamos - Tienda") }) },
        floatingActionButton = {
            if (pantalla == "lista")
                FloatingActionButton(onClick = { pantalla = "form" }) { Text("+") }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            AnimatedVisibility(visible = mensaje != null) {
                AssistChip(
                    onClick = { vm.clearMensaje() },
                    label = { Text(mensaje ?: "") },
                    modifier = Modifier.padding(8.dp)
                )
            }
            when (pantalla) {
                "lista" -> ListaReclamos(reclamos) { pantalla = "form" }
                "form" -> FormReclamo(
                    fotoUri = fotoUri,
                    latLong = latLong,
                    onSetFoto = { vm.setFoto(it) },
                    onSetUbicacion = { lat, lon -> vm.setUbicacion(lat, lon) },
                    onGuardar = { n, d, c, e, tel, nro, suc ->
                        vm.guardar(n, d, c, e, tel, nro, suc)
                        pantalla = "lista"
                    },
                    onCancelar = { pantalla = "lista" }
                )
            }
        }
    }
}

@Composable
private fun ListaReclamos(items: List<Reclamo>, onNuevo: () -> Unit) {
    if (items.isEmpty()) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No hay reclamos")
            Spacer(Modifier.height(8.dp))
            Button(onClick = onNuevo) { Text("Nuevo reclamo") }
        }
    } else {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { r ->
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(r.nombre, style = MaterialTheme.typography.titleMedium)
                        Text("${r.categoria} · ${r.sucursal ?: "Sin sucursal"}")
                        Spacer(Modifier.height(4.dp))
                        Text(r.descripcion, style = MaterialTheme.typography.bodyMedium)
                        r.fotoUri?.let {
                            Spacer(Modifier.height(8.dp))
                            AsyncImage(
                                model = it,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                            )
                        }
                        if (r.latitud != null && r.longitud != null) {
                            Text("Ubicación: ${r.latitud}, ${r.longitud}")
                        }
                        r.nroCompra?.let { Text("Nº compra: $it") }
                        r.telefono?.let { Text("Tel: $it") }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormReclamo(
    fotoUri: String?,
    latLong: Pair<Double, Double>?,
    onSetFoto: (String?) -> Unit,
    onSetUbicacion: (Double, Double) -> Unit,
    onGuardar: (String, String, String, String, String?, String?, String?) -> Unit,
    onCancelar: () -> Unit
) {
    // === Conectar CÁMARA y GPS (no cambia el diseño) ===
    val takePhoto = rememberTakePhotoLauncher { uri -> onSetFoto(uri) }
    val requestLocation = rememberLocationRequester { lat, lon -> onSetUbicacion(lat, lon) }

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    val categorias = listOf("Producto", "Atención", "Entrega", "Devolución")
    var categoria by remember { mutableStateOf(categorias.first()) }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var nroCompra by remember { mutableStateOf("") }
    var sucursal by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Nuevo reclamo", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = nombre, onValueChange = { nombre = it },
            label = { Text("Nombre *") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = descripcion, onValueChange = { descripcion = it },
            label = { Text("Descripción * (mín. 10)") }, modifier = Modifier.fillMaxWidth()
        )

        // Selector de categoría (Exposed Dropdown)
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = categoria, onValueChange = {},
                label = { Text("Categoría *") },
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                categorias.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = { categoria = it; expanded = false }
                    )
                }
            }
        }

        OutlinedTextField(email, { email = it }, label = { Text("Email *") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(telefono, { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(nroCompra, { nroCompra = it }, label = { Text("N° compra/boleta") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(sucursal, { sucursal = it }, label = { Text("Sucursal") }, modifier = Modifier.fillMaxWidth())

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Antes: Button(onClick = { /* TODO: CameraX */ onSetFoto(null) })
            Button(onClick = takePhoto) { Text("Tomar foto") }
            // Antes: Button(onClick = { latLong?.let { (la, lo) -> onSetUbicacion(la, lo) } })
            Button(onClick = requestLocation) { Text("Usar ubicación") }
        }

        // (Opcional) Vista previa de foto y coordenadas
        fotoUri?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )
        }
        latLong?.let { (la, lo) ->
            Text("Ubicación actual: $la, $lo")
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onCancelar) { Text("Cancelar") }
            Button(onClick = {
                onGuardar(
                    nombre, descripcion, categoria, email,
                    telefono.ifBlank { null }, nroCompra.ifBlank { null }, sucursal.ifBlank { null }
                )
            }) { Text("Enviar") }
        }
    }
}
