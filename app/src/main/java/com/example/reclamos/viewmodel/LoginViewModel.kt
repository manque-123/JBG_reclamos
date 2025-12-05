package com.example.reclamos.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reclamos.data.network.ApiService
import com.example.reclamos.data.network.TokenManager
import kotlinx.coroutines.launch

class LoginViewModel(private val api: ApiService) : ViewModel() {

    val loading = MutableLiveData(false)
    val mensajeError = MutableLiveData<String?>(null)

    fun login(correo: String, password: String, context: Context) {
        viewModelScope.launch {

            try {
                loading.value = true

                val body = mapOf(
                    "correo" to correo,
                    "password" to password
                )

                val response = api.login(body)

                if (response.isSuccessful) {

                    val token = response.body()?.get("token")
                    if (token != null) {
                        TokenManager.saveToken(context, token)
                        mensajeError.value = null
                    } else {
                        mensajeError.value = "Error: token no encontrado"
                    }

                } else {
                    mensajeError.value = "Credenciales incorrectas"
                }

            } catch (e: Exception) {
                mensajeError.value = "Error de conexión"
            } finally {
                loading.value = false
            }
        }
    }
}
