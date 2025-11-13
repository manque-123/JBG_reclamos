package com.example.reclamos.ui

import com.google.android.gms.location.Priority
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import java.io.File

// ----------------------------
//  CÁMARA
// ----------------------------
@Composable
fun rememberTakePhotoLauncher(
    onImage: (String?) -> Unit
): () -> Unit {

    val ctx = LocalContext.current
    var pendingUri by remember { mutableStateOf<Uri?>(null) }

    val takePicture = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { ok ->
        onImage(if (ok) pendingUri?.toString() else null)
    }

    val reqCameraPerm = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createTempImageUri(ctx)
            pendingUri = uri
            takePicture.launch(uri)
        } else onImage(null)
    }

    return {
        val granted = ContextCompat.checkSelfPermission(
            ctx, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            val uri = createTempImageUri(ctx)
            pendingUri = uri
            takePicture.launch(uri)
        } else {
            reqCameraPerm.launch(Manifest.permission.CAMERA)
        }
    }
}

private fun createTempImageUri(context: Context): Uri {

    // 🔥 IMPORTANTE: Carpeta coincide con file_paths.xml
    val dir = File(context.cacheDir, "images").apply { mkdirs() }

    val file = File(
        dir,
        "IMG_${System.currentTimeMillis()}.jpg"
    )

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",   // authority correcto
        file
    )
}

// ----------------------------
//  GPS
// ----------------------------
@Composable
fun rememberLocationRequester(
    onLocation: (Double, Double) -> Unit
): () -> Unit {
    val ctx = LocalContext.current

    val reqPerms = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { res ->
        val ok = res[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                res[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (ok) requestCurrentLocationRobust(ctx, onLocation)
    }

    return {
        val fine = ContextCompat.checkSelfPermission(
            ctx, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarse = ContextCompat.checkSelfPermission(
            ctx, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (fine == PackageManager.PERMISSION_GRANTED ||
            coarse == PackageManager.PERMISSION_GRANTED
        ) {
            requestCurrentLocationRobust(ctx, onLocation)
        } else {
            reqPerms.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
private fun requestCurrentLocationRobust(
    context: Context,
    onLocation: (Double, Double) -> Unit
) {
    val fused = LocationServices.getFusedLocationProviderClient(context)
    val cts = CancellationTokenSource()

    fused.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
        .addOnSuccessListener { loc ->
            if (loc != null) {
                onLocation(loc.latitude, loc.longitude)
            } else {
                fused.lastLocation
                    .addOnSuccessListener { last ->
                        if (last != null) onLocation(last.latitude, last.longitude)
                        else requestSingleUpdate(context, onLocation)
                    }
                    .addOnFailureListener { requestSingleUpdate(context, onLocation) }
            }
        }
        .addOnFailureListener { requestSingleUpdate(context, onLocation) }
}

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
private fun requestSingleUpdate(
    context: Context,
    onLocation: (Double, Double) -> Unit
) {
    val fused = LocationServices.getFusedLocationProviderClient(context)

    val req = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        1500L
    )
        .setMaxUpdates(1)
        .build()

    val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val loc = result.lastLocation
            if (loc != null) {
                onLocation(loc.latitude, loc.longitude)
            }
            fused.removeLocationUpdates(this)   // ← importante
        }
    }

    fused.requestLocationUpdates(
        req,
        callback,
        context.mainLooper
    )
}

