package com.luridevlabs.citylights.presentation.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel

@Composable
fun Navigation(monumentsViewModel: MonumentsViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "monumentList") {
        composable("monumentList") {
            MonumentList(navController, monumentsViewModel)
        }
        composable(
            route = "monumentDetail/{monumentId}",
            arguments = listOf(navArgument("monumentId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val monumentId = backStackEntry.arguments?.getString("monumentId")
            requireNotNull(monumentId)
            MonumentDetail(monumentId, monumentsViewModel)
        }
    }
}