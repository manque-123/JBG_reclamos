package com.example.reclamos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.reclamos.data.network.TokenManager
import com.example.reclamos.ui.screens.*

@Composable
fun Navigation(navController: NavHostController) {

    val context = LocalContext.current
    val hasToken = TokenManager.getToken(context) != null
    val startDest = if (hasToken) "listaReclamos" else "login"

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("listaReclamos") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("listaReclamos") {
            ReclamosListScreen(
                onAgregarClick = { navController.navigate("crearReclamo") },
                onEditarClick = { id -> navController.navigate("editarReclamo/$id") },
                onVerDetalle = { id -> navController.navigate("detalleReclamo/$id") },
                onLogout = {
                    TokenManager.clearToken(context)
                    navController.navigate("login") {
                        popUpTo("listaReclamos") { inclusive = true }
                    }
                }
            )
        }

        composable("crearReclamo") {
            ReclamoCrearScreen(onBack = { navController.popBackStack() })
        }

        composable("editarReclamo/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0L
            ReclamoEditarScreen(id = id, onBack = { navController.popBackStack() })
        }

        composable("detalleReclamo/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0L
            DetalleReclamoScreen(id = id, onBack = { navController.popBackStack() })
        }

    }
}
