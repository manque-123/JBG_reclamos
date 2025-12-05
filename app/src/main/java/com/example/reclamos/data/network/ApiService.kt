package com.example.reclamos.data.network

import com.example.reclamos.model.Reclamo
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun login(@Body body: Map<String, String>): Response<Map<String, String>>

    @GET("reclamos")
    suspend fun obtenerReclamos(): Response<List<Reclamo>>

    @POST("reclamos")
    suspend fun crearReclamo(@Body reclamo: Reclamo): Response<Map<String, Int>>

    @PUT("reclamos/{id}")
    suspend fun actualizarReclamo(
        @Path("id") id: Int,
        @Body reclamo: Reclamo
    ): Response<Map<String, Int>>

    @DELETE("reclamos/{id}")
    suspend fun eliminarReclamo(@Path("id") id: Int): Response<Unit>
}
