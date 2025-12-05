package com.example.reclamos.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reclamos.data.network.ReclamoRepository
import com.example.reclamos.model.Reclamo
import kotlinx.coroutines.launch

class ReclamosViewModel(private val repo: ReclamoRepository) : ViewModel() {

    val listaReclamos = MutableLiveData<List<Reclamo>>(emptyList())
    val loading = MutableLiveData(false)

    fun cargarReclamos() {
        viewModelScope.launch {
            loading.value = true
            val data = repo.obtenerReclamos()
            listaReclamos.value = data ?: emptyList()
            loading.value = false
        }
    }

    fun crearReclamo(r: Reclamo) {
        viewModelScope.launch {
            val ok = repo.crearReclamo(r)
            if (ok) cargarReclamos()
        }
    }

    fun editarReclamo(id: Long, r: Reclamo) {
        viewModelScope.launch {
            val ok = repo.editarReclamo(id.toInt(), r)
            if (ok) cargarReclamos()
        }
    }

    fun eliminarReclamo(id: Long) {
        viewModelScope.launch {
            val ok = repo.eliminarReclamo(id.toInt())
            if (ok) cargarReclamos()
        }
    }
}
