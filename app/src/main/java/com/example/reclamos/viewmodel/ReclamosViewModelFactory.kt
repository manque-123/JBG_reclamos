package com.example.reclamos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.reclamos.data.network.ApiService
import com.example.reclamos.data.network.ReclamoRepository

class ReclamosViewModelFactory(private val api: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReclamosViewModel::class.java)) {
            val repo = ReclamoRepository(api)   // 🔥 AQUÍ SE CORRIGE
            return ReclamosViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
