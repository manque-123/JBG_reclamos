package com.example.reclamos.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.io.File

@Composable
fun HardwareTestScreen() {
    val ctx = LocalContext.current


    var camStatus by remember { mutableStateOf("Cámara: lista") }
    var gpsStatus by remember { mutableStateOf("GPS: listo") }
    var photoUri by remember { mutableStateOf<String?>(null) }
    var coords by remember { mutableStateOf<String?>(null) }

    var pendingUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { ok ->
        camStatus = if (ok) "Cámara: OK" else "Cámara: cancelado/fallo"
        photoUri = if (ok) pendingUri?.toString() else null
    }

    val cameraPermLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            // Crear archivo/URI y lanzar cámara
            val uri = createTempImageUri(ctx)
            pendingUri = uri
            takePictureLauncher.launch(uri)
        } else {
            camStatus = "Cámara: permiso denegado"
        }
    }

    val locationPermsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { res ->
        val ok = res[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                res[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (ok) {
            gpsStatus = "GPS: solicitando ubicación…"
            requestCurrentLocation(ctx) { lat, lon ->
                coords = if (lat != null && lon != null) "$lat , $lon" else null
                gpsStatus = if (coords != null) "GPS: OK" else "GPS: sin fix (activa ubicación del teléfono)"
            }
        } else {
            gpsStatus = "GPS: permiso denegado"
        }
    }


    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Prueba de Cámara y GPS", style = MaterialTheme.typography.titleLarge)

        // CÁMARA
        Text(camStatus)
        Button(onClick = {
            val granted = ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
            if (granted) {
                val uri = createTempImageUri(ctx)
                pendingUri = uri
                takePictureLauncher.launch(uri)
            } else {
                cameraPermLauncher.launch(Manifest.permission.CAMERA)
            }
        }) { Text("Tomar foto (prueba)") }

        photoUri?.let {
            Spacer(Modifier.height(8.dp))
            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(180.dp)
            )
            Text(it)
        }

        Divider()

        // GPS
        Text(gpsStatus)
        Button(onClick = {
            val fine = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
            val coarse = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION)
            if (fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED) {
                gpsStatus = "GPS: solicitando ubicación…"
                requestCurrentLocation(ctx) { lat, lon ->
                    coords = if (lat != null && lon != null) "$lat , $lon" else null
                    gpsStatus = if (coords != null) "GPS: OK" else "GPS: sin fix (activa ubicación del teléfono)"
                }
            } else {
                locationPermsLauncher.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))
            }
        }) { Text("Obtener ubicación (prueba)") }

        coords?.let { Text("Lat, Lon: $it") }
    }
}


private fun createTempImageUri(context: Context): Uri {
    val dir = File(context.cacheDir, "images").apply { mkdirs() }
    val file = File(dir, "IMG_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}

private fun requestCurrentLocation(
    context: Context,
    cb: (Double?, Double?) -> Unit
) {
    val fused = LocationServices.getFusedLocationProviderClient(context)

    val cts = com.google.android.gms.tasks.CancellationTokenSource()
    fused.getCurrentLocation(
        com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
        cts.token
    ).addOnSuccessListener { loc ->
        if (loc != null) {
            cb(loc.latitude, loc.longitude)
        } else {
            fused.lastLocation
                .addOnSuccessListener { last ->
                    if (last != null) {
                        cb(last.latitude, last.longitude)
                    } else {
                        // 3) Último recurso: una sola actualización
                        requestSingleUpdate(context, cb)
                    }
                }
                .addOnFailureListener { requestSingleUpdate(context, cb) }
        }
    }.addOnFailureListener {
        requestSingleUpdate(context, cb)
    }
}

private fun requestSingleUpdate(
    context: Context,
    cb: (Double?, Double?) -> Unit
) {
    val fused = LocationServices.getFusedLocationProviderClient(context)
    val request = com.google.android.gms.location.LocationRequest.Builder(
        com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
        1500L // intervalo sugerido, no estricto
    )
        .setMaxUpdates(1)      // una sola lectura
        .setMinUpdateIntervalMillis(0)
        .build()

    val callback = object : com.google.android.gms.location.LocationCallback() {
        override fun onLocationResult(result: com.google.android.gms.location.LocationResult) {
            fused.removeLocationUpdates(this)
            val loc = result.lastLocation
            cb(loc?.latitude, loc?.longitude)
        }

        override fun onLocationAvailability(availability: com.google.android.gms.location.LocationAvailability) {
            // Si no está disponible, corta para no quedar enganchado
            if (!availability.isLocationAvailable) {
                fused.removeLocationUpdates(this)
                cb(null, null)
            }
        }
    }

    try {
        fused.requestLocationUpdates(
            request,
            callback,
            android.os.Looper.getMainLooper()
        )
    } catch (_: SecurityException) {
        // por si faltara el permiso (no debería, ya lo pedimos antes)
        cb(null, null)
    }
}
