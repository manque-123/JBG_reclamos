package com.example.reclamos.data.network

import android.util.Log
import com.example.reclamos.model.NominatimResponse
import com.example.reclamos.model.Reclamo
import retrofit2.Response

class ReclamoRepository(private val api: ApiService) {

    // Obtener
    suspend fun obtenerReclamos(): List<Reclamo>? {
        return try {
            val response = api.obtenerReclamos()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.e("Repo", "Error obteniendo reclamos", e)
            null
        }
    }

    // Crear
    suspend fun crearReclamo(reclamo: Reclamo): Int? {
        return try {
            val response = api.crearReclamo(reclamo)
            if (response.isSuccessful) {
                response.body()?.get("id")
            } else null
        } catch (e: Exception) {
            Log.e("Repo", "Error creando reclamo", e)
            null
        }
    }

    // Actualizar
    suspend fun actualizarReclamo(id: Int, reclamo: Reclamo): Boolean {
        return try {
            val response = api.actualizarReclamo(id, reclamo)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("Repo", "Error actualizando reclamo", e)
            false
        }
    }

    // Eliminar
    suspend fun eliminarReclamo(id: Int): Boolean {
        return try {
            val response = api.eliminarReclamo(id)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("Repo", "Error eliminando reclamo", e)
            false
        }
    }

    // API extern nominatim
    suspend fun obtenerDireccion(lat: Double, lon: Double): NominatimResponse? {
        return try {
            val response: Response<NominatimResponse> = api.obtenerDireccion(lat, lon)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.e("Repo", "Error obteniendo dirección", e)
            null
        }
    }
}
