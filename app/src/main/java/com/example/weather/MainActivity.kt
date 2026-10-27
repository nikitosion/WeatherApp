package com.example.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weather.ui.common.WeatherAppTopAppBar
import com.example.weather.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherApp()
        }
    }
}

@Composable
fun WeatherApp() {
    val navController = rememberNavController()

    val currentScreen = navController.currentBackStackEntryAsState().value?.destination?.route

    WeatherAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (currentScreen == Screen.DayForecast.route) {
                    WeatherAppTopAppBar(
                        title = stringResource(R.string.day_forecast_title),
                        onBackClick = { navController.navigateUp() }
                    )
                }
            }) { innerPadding ->
            Surface(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
            ) {
                NavGraph(navController)
            }
        }
    }
}

@Preview
@Composable
fun WeatherAppPreview() {
    WeatherApp()
}