package com.example.reclamos.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reclamos.data.network.ApiClient
import com.example.reclamos.model.Reclamo
import kotlinx.coroutines.launch

class ReclamosViewModel : ViewModel() {

    val listaReclamos = MutableLiveData<List<Reclamo>>()
    val mensaje = MutableLiveData<String>()

    fun cargarReclamos() {
        viewModelScope.launch {
            try {
                listaReclamos.postValue(ApiClient.api.obtenerReclamos())
            } catch (e: Exception) {
                mensaje.postValue("Error: ${e.message}")
            }
        }
    }

    fun crearReclamo(reclamo: Reclamo, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                ApiClient.api.crearReclamo(reclamo)
                cargarReclamos()
                onSuccess()
            } catch (e: Exception) {
                mensaje.postValue("Error: ${e.message}")
            }
        }
    }

    fun actualizarReclamo(id: Long, reclamo: Reclamo, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                ApiClient.api.actualizarReclamo(id, reclamo)
                cargarReclamos()
                onSuccess()
            } catch (e: Exception) {
                mensaje.postValue("Error: ${e.message}")
            }
        }
    }

    fun eliminarReclamo(id: Long) {
        viewModelScope.launch {
            try {
                ApiClient.api.eliminarReclamo(id)
                cargarReclamos()
            } catch (e: Exception) {
                mensaje.postValue("Error: ${e.message}")
            }
        }
    }
}
