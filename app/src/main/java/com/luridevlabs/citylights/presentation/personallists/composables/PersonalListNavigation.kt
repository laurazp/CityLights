package com.luridevlabs.citylights.presentation.personallists.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.luridevlabs.citylights.presentation.common.composables.MonumentDetail

@Composable
fun PersonalListNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "personalMonumentList") {
        composable("personalMonumentList") {
            MonumentList(navController)
        }
        composable(
            route = "monumentDetail/{monumentId}",
            arguments = listOf(navArgument("monumentId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val monumentId = backStackEntry.arguments?.getString("monumentId")
            requireNotNull(monumentId)
            MonumentDetail(navController, monumentId)
        }
    }
}