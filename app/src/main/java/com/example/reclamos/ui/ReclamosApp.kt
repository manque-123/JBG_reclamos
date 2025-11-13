package com.example.reclamos.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
    val guardando by vm.guardando.observeAsState(false)

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

            AnimatedContent(
                targetState = pantalla,
                transitionSpec = {
                    fadeIn(animationSpec = tween(250)) togetherWith
                            fadeOut(animationSpec = tween(250))
                }
            ) { screen ->

                when (screen) {
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
                        onCancelar = { pantalla = "lista" },
                        guardando = guardando
                    )
                }
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
            Modifier.fillMaxSize().padding(12.dp),
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
                                modifier = Modifier.fillMaxWidth().height(160.dp)
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
    onCancelar: () -> Unit,
    guardando: Boolean
) {
    val takePhoto = rememberTakePhotoLauncher { uri -> onSetFoto(uri) }
    val requestLocation = rememberLocationRequester { lat, lon -> onSetUbicacion(lat, lon) }

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var nroCompra by remember { mutableStateOf("") }
    var sucursal by remember { mutableStateOf("") }

    val categorias = listOf("Producto", "Atención", "Entrega", "Devolución")
    var categoria by remember { mutableStateOf(categorias.first()) }

    var errorNombre by remember { mutableStateOf(false) }
    var errorDesc by remember { mutableStateOf(false) }
    var errorEmail by remember { mutableStateOf(false) }

    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Text("Nuevo reclamo", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                errorNombre = it.isBlank()
            },
            isError = errorNombre,
            label = { Text("Nombre *") },
            modifier = Modifier.fillMaxWidth()
        )
        if (errorNombre) Text("Ingresa un nombre.", color = MaterialTheme.colorScheme.error)

        OutlinedTextField(
            value = descripcion,
            onValueChange = {
                descripcion = it
                errorDesc = it.length < 10
            },
            isError = errorDesc,
            label = { Text("Descripción * (mín. 10)") },
            modifier = Modifier.fillMaxWidth()
        )
        if (errorDesc) Text("Mínimo 10 caracteres.", color = MaterialTheme.colorScheme.error)

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = categoria,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoría *") },
                modifier = Modifier.menuAnchor().fillMaxWidth()
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

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorEmail = !emailRegex.matches(it)
            },
            isError = errorEmail,
            label = { Text("Email *") },
            modifier = Modifier.fillMaxWidth()
        )
        if (errorEmail) Text("Email inválido.", color = MaterialTheme.colorScheme.error)

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = nroCompra,
            onValueChange = { nroCompra = it },
            label = { Text("N° compra/boleta") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = sucursal,
            onValueChange = { sucursal = it },
            label = { Text("Sucursal") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = takePhoto, enabled = !guardando) { Text("Tomar foto") }
            Button(onClick = requestLocation, enabled = !guardando) { Text("Usar ubicación") }
        }

        fotoUri?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(160.dp)
            )
        }

        latLong?.let { (la, lo) ->
            Text("Ubicación actual: $la, $lo")
        }

        var animate by remember { mutableStateOf(false) }
        val scale by animateFloatAsState(
            targetValue = if (animate) 0.9f else 1f,
            animationSpec = tween(150),
            finishedListener = { animate = false }
        )

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(
                onClick = onCancelar,
                enabled = !guardando
            ) { Text("Cancelar") }

            Button(
                onClick = {
                    animate = true

                    errorNombre = nombre.isBlank()
                    errorDesc = descripcion.length < 10
                    errorEmail = !emailRegex.matches(email)

                    if (!errorNombre && !errorDesc && !errorEmail) {
                        onGuardar(
                            nombre,
                            descripcion,
                            categoria,
                            email,
                            telefono.ifBlank { null },
                            nroCompra.ifBlank { null },
                            sucursal.ifBlank { null }
                        )
                    }
                },
                enabled = !guardando,
                modifier = Modifier.scale(scale)
            ) {
                if (guardando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Enviar")
                }
            }
        }
    }
}
