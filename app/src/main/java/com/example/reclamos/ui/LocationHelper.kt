package com.example.reclamos.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker

@SuppressLint("MissingPermission")
@Composable
fun rememberUserLocation(context: Context): Location? {
    var location by remember { mutableStateOf<Location?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED
        if (!granted) launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        else {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }
    }
    return location
}
