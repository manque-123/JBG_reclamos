package com.example.reclamos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.reclamos.ui.screens.*



@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = "lista") {

        composable("lista") {
            ReclamosListScreen(
                onAgregarClick = { navController.navigate("crear") },
                onEditarClick = { id -> navController.navigate("editar/$id") }
            )
        }

        composable("crear") {
            ReclamoCrearScreen(onBack = { navController.popBackStack() })
        }

        composable("editar/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")!!.toLong()
            ReclamoEditarScreen(id = id, onBack = { navController.popBackStack() })
        }

        composable("pokemon") {
            PokemonScreen()
        }
    }
}
