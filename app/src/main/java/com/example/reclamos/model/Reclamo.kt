package com.example.reclamos.model

data class Reclamo(
    val id: Long? = null,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val email: String,
    val telefono: String? = null,
    val nroCompra: String? = null,
    val sucursal: String? = null,
    val fotoUri: String? = null,      // NUEVO
    val latitud: Double? = null,      // NUEVO
    val longitud: Double? = null      // NUEVO
)
