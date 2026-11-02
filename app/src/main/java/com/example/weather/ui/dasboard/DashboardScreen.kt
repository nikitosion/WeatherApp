package com.example.weather.ui.dasboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weather.R
import com.example.weather.domain.ForecastError
import com.example.weather.domain.model.Coordinates
import com.example.weather.domain.model.DayForecast
import com.example.weather.domain.model.Location
import com.example.weather.ui.common.BigWeatherBlock
import com.example.weather.ui.theme.WeatherAppTheme
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@Composable
fun DashboardScreenRoute(
    viewModel: DashboardViewModel = koinViewModel(),
    onDayClick: (LocalDate) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value
    DashboardScreen(
        uiState = uiState,
        onRetry = { viewModel.retryLoadForecast() },
        onDayClick = onDayClick
    )
}

@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onRetry: () -> Unit,
    onDayClick: (LocalDate) -> Unit
) {
    DashboardScreenContent(
        isLoading = uiState.isLoading,
        weekForecast = uiState.weekForecast,
        location = uiState.location,
        error = uiState.error,
        onRetry = onRetry,
        onDayClick = onDayClick
    )
}

@Composable
fun DashboardScreenContent(
    isLoading: Boolean = false,
    weekForecast: List<DayForecast>? = null,
    location: Location? = null,
    error: ForecastError? = null,
    onRetry: () -> Unit = {},
    onDayClick: (LocalDate) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = location?.displayName ?: stringResource(R.string.no_location_selected),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Normal
            )
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                onClick = { /* Handle click */ }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.location_on_24),
                    contentDescription = "Dropdown Icon"
                )
            }
        }

        if (location != null) {

            if (isLoading) {
                ForecastStatusCard(
                    title = stringResource(R.string.forecast_loading),
                    message = stringResource(R.string.forecast_loading_description),
                    isLoading = true,
                    onRetry = onRetry
                )
            } else if (error != null) {
                val errorMessage = when (error) {
                    ForecastError.NETWORK -> stringResource(R.string.forecast_error_network)
                    ForecastError.SERVICE -> stringResource(R.string.forecast_error_service)
                    ForecastError.INVALID_DATA -> stringResource(R.string.forecast_error_invalid_data)
                    ForecastError.UNKNOWN -> stringResource(R.string.forecast_error_unknown)
                }

                ForecastStatusCard(
                    title = stringResource(R.string.forecast_error),
                    message = errorMessage,
                    onRetry = onRetry
                )
            } else if (weekForecast.isNullOrEmpty()) {
                ForecastStatusCard(
                    title = stringResource(R.string.forecast_empty),
                    message = stringResource(R.string.forecast_empty_description),
                    onRetry = onRetry
                )
            } else {
                val todayForecast = weekForecast.first()

                BigWeatherBlock(
                    modifier = Modifier.padding(vertical = 16.dp),
                    dayForecast = todayForecast,
                    onDayClick = { onDayClick(todayForecast.date) }
                )

                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = "Next days",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Normal
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (dayForecast in weekForecast.drop(1)) {
                        WeekDayForecast(
                            modifier = Modifier,
                            dayForecast = dayForecast,
                            onDayClick = { onDayClick(dayForecast.date) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DashboardScreenPreview() {
    val weekForecast = listOf(
        DayForecast(
            date = LocalDate.now().plusDays(1),
            temperature = 10.0,
            weatherCondition = "Sunny"
        ),
        DayForecast(
            date = LocalDate.now().plusDays(2),
            temperature = 12.0,
            weatherCondition = "Cloudy"
        ),
        DayForecast(
            date = LocalDate.now().plusDays(3),
            temperature = 8.0,
            weatherCondition = "Rainy"
        ),
        DayForecast(
            date = LocalDate.now().plusDays(4),
            temperature = 9.0,
            weatherCondition = "Windy"
        ),
        DayForecast(
            date = LocalDate.now().plusDays(5),
            temperature = 11.0,
            weatherCondition = "Snowy"
        ),
        DayForecast(
            date = LocalDate.now().plusDays(6),
            temperature = 7.0,
            weatherCondition = "Foggy"
        ),
        DayForecast(
            date = LocalDate.now().plusDays(7),
            temperature = 6.0,
            weatherCondition = "Stormy"
        )
    )

    WeatherAppTheme {
        DashboardScreenContent(
            location = Location(
                displayName = "Moscow",
                coordinates = Coordinates(latitude = 55.7512, longitude = 37.6184),
                detectedTimeMillis = System.currentTimeMillis()
            ),
            weekForecast = weekForecast,
            onDayClick = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
fun NullDashboardScreenPreview() {
    WeatherAppTheme {
        DashboardScreenContent(
            location = Location(
                displayName = "Moscow",
                coordinates = Coordinates(latitude = 55.7512, longitude = 37.6184),
                detectedTimeMillis = System.currentTimeMillis()
            ),
            onDayClick = {},
            weekForecast = null
        )
    }
}

@Composable
@Preview(showBackground = true)
fun LoadingDashboardScreenPreview() {
    WeatherAppTheme {
        DashboardScreenContent(
            location = Location(
                displayName = "Moscow",
                coordinates = Coordinates(latitude = 55.7512, longitude = 37.6184),
                detectedTimeMillis = System.currentTimeMillis()
            ),
            onDayClick = {},
            isLoading = true
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ErrorDashboardScreenPreview() {
    WeatherAppTheme {
        DashboardScreenContent(
            location = Location(
                displayName = "Moscow",
                coordinates = Coordinates(latitude = 55.7512, longitude = 37.6184),
                detectedTimeMillis = System.currentTimeMillis()
            ),
            onDayClick = {},
            isLoading = false,
            error = ForecastError.NETWORK
        )
    }
}

@Composable
@Preview(showBackground = true)
fun NoLocationDashboardScreenPreview() {
    WeatherAppTheme {
        DashboardScreenContent(
            location = null,
            onDayClick = {},
            isLoading = false,
            error = null
        )
    }
}
