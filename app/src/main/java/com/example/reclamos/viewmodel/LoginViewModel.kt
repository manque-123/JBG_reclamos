package com.example.reclamos.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reclamos.data.network.ApiClient
import com.example.reclamos.data.network.TokenManager
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    val mensaje = MutableLiveData<String?>(null)
    val cargando = MutableLiveData<Boolean>(false)

    fun login(
        context: Context,
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                cargando.postValue(true)

                val body = mapOf(
                    "email" to email,
                    "password" to password
                )

                val response = ApiClient.apiService.login(body)

                if (response.isSuccessful) {
                    val token = response.body()?.get("token")
                    if (token != null) {
                        TokenManager.saveToken(context, token)
                        onSuccess()
                    } else {
                        mensaje.postValue("Respuesta inválida del servidor")
                    }
                } else {
                    mensaje.postValue("Credenciales incorrectas")
                }

            } catch (e: Exception) {
                mensaje.postValue("Error: ${e.message}")
            } finally {
                cargando.postValue(false)
            }
        }
    }
}
