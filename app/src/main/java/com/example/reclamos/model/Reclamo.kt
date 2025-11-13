package com.example.reclamos.model

data class Reclamo(
    val id: Long = 0,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val email: String,
    val telefono: String?,
    val nroCompra: String?,
    val sucursal: String?,
    val fotoUri: String?,
    val latitud: Double?,
    val longitud: Double?
)
