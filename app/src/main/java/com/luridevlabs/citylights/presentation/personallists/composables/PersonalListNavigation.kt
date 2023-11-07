package com.luridevlabs.citylights.presentation.personallists.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel

@Composable
fun PersonalListNavigation(viewModel: MonumentsViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "personalMonumentList") {
        composable("personalMonumentList") { //backStackEntry ->
            //val listId = backStackEntry.arguments?.getString("listId")
            //requireNotNull(listId)
            PersonalMonumentList(navController, viewModel)
        }
        composable(
            route = "monumentDetail/{monumentId}",
            arguments = listOf(navArgument("monumentId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val monumentId = backStackEntry.arguments?.getString("monumentId")
            requireNotNull(monumentId)
            //TODO: cambiar¿?
            PersonalListMonumentDetail(navController, viewModel, monumentId)
        }
    }
}