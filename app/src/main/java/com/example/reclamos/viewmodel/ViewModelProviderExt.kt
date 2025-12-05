package com.example.reclamos.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.ViewModelStoreOwner

@Composable
inline fun <reified T : ViewModel> getViewModel(
    factory: ViewModelProvider.Factory
): T {
    return ViewModelProvider(LocalContext.current as ViewModelStoreOwner, factory)[T::class.java]
}
