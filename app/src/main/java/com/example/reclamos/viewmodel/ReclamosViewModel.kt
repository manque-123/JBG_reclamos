package com.example.reclamos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.reclamos.data.ReclamoRepository
import com.example.reclamos.model.Reclamo
import kotlinx.coroutines.launch

class ReclamosViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ReclamoRepository(app)

    private val _reclamos = MutableLiveData<List<Reclamo>>(emptyList())
    val reclamos: LiveData<List<Reclamo>> = _reclamos

    private val _mensaje = MutableLiveData<String?>(null)
    val mensaje: LiveData<String?> = _mensaje

    private val _fotoUri = MutableLiveData<String?>(null)
    val fotoUri: LiveData<String?> = _fotoUri

    private val _latLong = MutableLiveData<Pair<Double, Double>?>(null)
    val latLong: LiveData<Pair<Double, Double>?> = _latLong

    fun cargar() = viewModelScope.launch { _reclamos.value = repo.listar() }
    fun setFoto(uri: String?) { _fotoUri.value = uri }
    fun setUbicacion(lat: Double, lon: Double) { _latLong.value = lat to lon }
    fun clearMensaje() { _mensaje.value = null }

    fun guardar(
        nombre: String,
        descripcion: String,
        categoria: String,
        email: String,
        telefono: String?,
        nroCompra: String?,
        sucursal: String?
    ) {
        val emailOk = Regex("^[\\w\\-.+]+@[\\w\\-]+\\.[A-Za-z]{2,}$").matches(email.trim())
        if (nombre.isBlank() || descripcion.length < 10 || categoria.isBlank() || !emailOk) {
            _mensaje.value = "Completa los campos obligatorios y un email válido."
            return
        }
        val (lat, lon) = _latLong.value ?: (null to null)
        val nuevo = Reclamo(
            nombre = nombre.trim(),
            descripcion = descripcion.trim(),
            categoria = categoria,
            email = email.trim(),
            telefono = telefono?.trim().takeIf { !it.isNullOrBlank() },
            nroCompra = nroCompra?.trim().takeIf { !it.isNullOrBlank() },
            sucursal = sucursal?.trim().takeIf { !it.isNullOrBlank() },
            fotoUri = _fotoUri.value,
            latitud = lat,
            longitud = lon
        )
        viewModelScope.launch {
            val id = repo.insertar(nuevo)
            _mensaje.value = if (id > 0) "Reclamo enviado ✅" else "Error al guardar"
            _fotoUri.postValue(null); _latLong.postValue(null); cargar()
        }
    }
}
