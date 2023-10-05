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
            MonumentList(monumentsViewModel, navController)
        }
        composable(
            route = "monumentDetail" + "/{monumentId}",
            arguments = listOf(navArgument("monumentId") {
                type = NavType.LongType
                defaultValue = 1})
        ) { backStackEntry ->
            val monumentId = backStackEntry.arguments?.getLong("monumentId")
            requireNotNull(monumentId)
            MonumentDetail(monumentId, monumentsViewModel)
        }
    }
}