package com.example.reclamos.data.network

import android.util.Log
import com.example.reclamos.model.NominatimResponse
import com.example.reclamos.model.Reclamo
import retrofit2.Response

class ReclamoRepository(private val api: ApiService) {

    // Obtener lista de reclamos
    suspend fun obtenerReclamos(): List<Reclamo>? {
        return try {
            val response = api.obtenerReclamos()
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("Repo", "Error cargando reclamos: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("Repo", "Excepción cargando reclamos: ${e.message}")
            null
        }
    }

    // Crear reclamo
    suspend fun crearReclamo(reclamo: Reclamo): Boolean {
        return try {
            val response = api.crearReclamo(reclamo)
            response.isSuccessful && response.body()?.get("id") != null
        } catch (e: Exception) {
            Log.e("Repo", "Error creando reclamo: ${e.message}")
            false
        }
    }

    // Editar reclamo
    suspend fun editarReclamo(id: Int, reclamo: Reclamo): Boolean {
        return try {
            val response = api.actualizarReclamo(id, reclamo)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("Repo", "Error actualizando reclamo: ${e.message}")
            false
        }
    }

    // Eliminar
    suspend fun eliminarReclamo(id: Int): Boolean {
        return try {
            val response = api.eliminarReclamo(id)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("Repo", "Error eliminando reclamo: ${e.message}")
            false
        }
    }

    // 🔥 ESTA FUNCIÓN FALTABA — YA AGREGADA
    // Devuelve una dirección falsa (o puedes implementar la real después)
    suspend fun obtenerDireccion(lat: Double, lon: Double): String {
        return try {
            // Si NO usas una API real, dejamos valor por defecto
            "Dirección no disponible"
        } catch (e: Exception) {
            "Error obteniendo dirección"
        }
    }
}
