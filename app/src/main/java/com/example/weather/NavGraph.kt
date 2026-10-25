package com.example.weather

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weather.ui.dasboard.DashboardScreenRoute

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
}

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreenRoute()
        }
    }
}