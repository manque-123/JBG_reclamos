package com.example.reclamos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reclamos.viewmodel.LoginViewModel
import com.example.reclamos.viewmodel.ReclamosViewModel
import com.example.reclamos.viewmodel.LoginViewModelFactory
import com.example.reclamos.viewmodel.ReclamosViewModelFactory
import com.example.reclamos.data.network.ApiClient
import com.example.reclamos.data.network.TokenManager
import com.example.reclamos.ui.screens.*

@Composable
fun AppNavigation(navController: NavHostController) {

    val context = LocalContext.current

    // API
    val api = ApiClient.apiService

    // VIEWMODELS
    val loginVM: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(api)
    )
    val reclamosVM: ReclamosViewModel = viewModel(
        factory = ReclamosViewModelFactory(api)
    )

    //PANTALLA INICIAL
    val startDest =
        if (TokenManager.getToken(context) != null) "listaReclamos"
        else "login"

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {

        // LOGIN SCREEN
        composable("login") {
            LoginScreen(
                loginVM = loginVM,
                onLoginSuccess = {
                    // 🔥 IMPORTANTE → Cargar reclamos ANTES de ir a la lista
                    reclamosVM.cargarReclamos()

                    navController.navigate("listaReclamos") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // LISTA RECLAMOS
        composable("listaReclamos") {
            ReclamosListScreen(
                viewModel = reclamosVM,
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

        // CREAR RECLAMO
        composable("crearReclamo") {
            ReclamoCrearScreen(
                navController = navController,
                reclamosVM = reclamosVM,
                onBack = { navController.popBackStack() }
            )
        }

        // EDITAR
        composable("editarReclamo/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toLong() ?: 0L

            ReclamoEditarScreen(
                id = id,
                navController = navController,
                reclamosVM = reclamosVM,
                onBack = { navController.popBackStack() }
            )
        }

        // DETALLE
        composable("detalleReclamo/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toLong() ?: 0L

            DetalleReclamoScreen(
                id = id,
                viewModel = reclamosVM,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
