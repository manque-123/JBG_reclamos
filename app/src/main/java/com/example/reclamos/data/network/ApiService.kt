package com.example.reclamos.data.network

import com.example.reclamos.model.Reclamo
import retrofit2.http.*

interface ApiService {

    @GET("reclamos")
    suspend fun obtenerReclamos(): List<Reclamo>

    @POST("reclamos")
    suspend fun crearReclamo(@Body reclamo: Reclamo): Reclamo

    @PUT("reclamos/{id}")
    suspend fun actualizarReclamo(@Path("id") id: Long, @Body reclamo: Reclamo): Reclamo

    @DELETE("reclamos/{id}")
    suspend fun eliminarReclamo(@Path("id") id: Long)
}
