package com.example.reclamos.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reclamos.data.network.ApiClient
import com.example.reclamos.model.Producto
import kotlinx.coroutines.launch

class ProductosViewModel : ViewModel() {

    val productos = MutableLiveData<List<Producto>>()
    val mensaje = MutableLiveData<String>()

    fun cargarProductos() {
        viewModelScope.launch {
            try {
                val lista = ApiClient.api.obtenerProductos()
                productos.postValue(lista)
            } catch (e: Exception) {
                mensaje.postValue("Error: ${e.message}")
            }
        }
    }

    fun agregarProducto(nombre: String, precio: Double) {
        viewModelScope.launch {
            try {
                val nuevo = Producto(nombre = nombre, precio = precio)
                val productoCreado = ApiClient.api.crearProducto(nuevo)
                mensaje.postValue("Producto creado: ${productoCreado.nombre}")
                cargarProductos()
            } catch (e: Exception) {
                mensaje.postValue("Error: ${e.message}")
            }
        }
    }


}
