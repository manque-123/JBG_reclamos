package com.example.reclamos.model

data class Reclamo(
    val id: Long = 0,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val email: String,
    val telefono: String? = null,
    val nroCompra: String? = null,
    val sucursal: String? = null,
    val fotoUri: String? = null,
    val latitud: Double? = null,
    val longitud: Double? = null
)
