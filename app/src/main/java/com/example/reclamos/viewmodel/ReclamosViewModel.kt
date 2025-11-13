package com.example.reclamos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reclamos.data.ReclamoRepository
import com.example.reclamos.model.Reclamo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReclamosViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ReclamoRepository(app)

    val reclamos = MutableLiveData<List<Reclamo>>(emptyList())
    val mensaje = MutableLiveData<String?>(null)
    val fotoUri = MutableLiveData<String?>(null)
    val latLong = MutableLiveData<Pair<Double, Double>?>(null)
    val guardando = MutableLiveData(false)

    fun cargar() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repo.listar()
            reclamos.postValue(data)
        }
    }

    fun guardar(
        nombre: String,
        descripcion: String,
        categoria: String,
        email: String,
        telefono: String?,
        nroCompra: String?,
        sucursal: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            guardando.postValue(true)

            val reclamo = Reclamo(
                id = 0,
                nombre = nombre,
                descripcion = descripcion,
                categoria = categoria,
                email = email,
                telefono = telefono,
                nroCompra = nroCompra,
                sucursal = sucursal,
                fotoUri = fotoUri.value,
                latitud = latLong.value?.first,
                longitud = latLong.value?.second
            )

            repo.insertar(reclamo)

            guardando.postValue(false)

            mensaje.postValue("Reclamo guardado correctamente")
            cargar()

            fotoUri.postValue(null)
            latLong.postValue(null)
        }
    }

    fun setFoto(uri: String?) {
        fotoUri.value = uri
    }

    fun setUbicacion(lat: Double, lon: Double) {
        latLong.value = lat to lon
    }

    fun clearMensaje() {
        mensaje.value = null
    }
}
