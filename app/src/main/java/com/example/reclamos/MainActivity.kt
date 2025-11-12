package com.example.reclamos.viewmodel  // ← deja tu paquete igual si es este

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.reclamos.ui.ReclamosApp

class MainActivity : ComponentActivity() {

    private val vm: ReclamosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReclamosApp(vm)   // ← Muestra TU app (lista + formulario con cámara y gps)
        }
    }
}
