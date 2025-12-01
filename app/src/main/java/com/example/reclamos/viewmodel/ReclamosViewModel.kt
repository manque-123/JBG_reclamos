package com.example.reclamos.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reclamos.data.network.ApiClient
import com.example.reclamos.model.Reclamo
import kotlinx.coroutines.launch

class ReclamosViewModel : ViewModel() {

    val listaReclamos = MutableLiveData<List<Reclamo>>(emptyList())
    val loading = MutableLiveData<Boolean>(false)
    val mensaje = MutableLiveData<String?>(null)

    //Cargar
    fun cargarReclamos() {
        viewModelScope.launch {
            try {
                loading.postValue(true)

                val response = ApiClient.apiService.obtenerReclamos()

                if (response.isSuccessful) {
                    listaReclamos.postValue(response.body() ?: emptyList())
                } else {
                    mensaje.postValue("Error al cargar (${response.code()})")
                }

            } catch (e: Exception) {
                mensaje.postValue("Error: ${e.message}")
            } finally {
                loading.postValue(false)
            }
        }
    }

    //Eliminar
    fun eliminarReclamo(id: Long) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.eliminarReclamo(id.toInt())

                if (response.isSuccessful) {
                    cargarReclamos()
                } else {
                    mensaje.postValue("Error al eliminar (${response.code()})")
                }

            } catch (e: Exception) {
                mensaje.postValue("Error: ${e.message}")
            }
        }
    }

    //Crear
    fun crearReclamo(reclamo: Reclamo, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.crearReclamo(reclamo)

                if (response.isSuccessful) {
                    cargarReclamos()
                    onSuccess()
                } else {
                    mensaje.postValue("Error al crear (${response.code()})")
                }

            } catch (e: Exception) {
                mensaje.postValue("Error: ${e.message}")
            }
        }
    }

    //Editar
    fun actualizarReclamo(id: Long, reclamo: Reclamo, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.actualizarReclamo(id.toInt(), reclamo)

                if (response.isSuccessful) {
                    cargarReclamos()
                    onSuccess()
                } else {
                    mensaje.postValue("Error al actualizar (${response.code()})")
                }

            } catch (e: Exception) {
                mensaje.postValue("Error: ${e.message}")
            }
        }
    }
}
