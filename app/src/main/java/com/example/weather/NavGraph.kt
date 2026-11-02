package com.example.weather

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weather.ui.choose_location.ChooseLocationScreenRoute
import com.example.weather.ui.dasboard.DashboardScreenRoute
import com.example.weather.ui.day_forecast.DayForecastScreenRoute
import java.time.LocalDate

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object DayForecast : Screen("day/{date}")
    object ChooseLocation : Screen("choose_location")
}

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreenRoute(
                onDayClick = { date ->
                    navController.navigate(
                        Screen.DayForecast.route.replace(
                            "{date}",
                            date.toString()
                        )
                    )
                },
                onLocationClick = {
                    navController.navigate(Screen.ChooseLocation.route)
                }
            )
        }

        composable(Screen.DayForecast.route) { backStackEntry ->
            val rawDate = backStackEntry.arguments?.getString("date")

            val date = remember(rawDate) {
                rawDate?.let {
                    try {
                        LocalDate.parse(it)
                    } catch (e: Exception) {
                        null
                    }
                }
            }

            DayForecastScreenRoute(date)
        }

        composable(Screen.ChooseLocation.route) {
            ChooseLocationScreenRoute()
        }
    }
}