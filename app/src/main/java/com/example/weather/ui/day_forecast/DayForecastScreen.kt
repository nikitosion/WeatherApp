package com.example.weather.ui.day_forecast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weather.R
import com.example.weather.domain.model.DayForecast
import com.example.weather.domain.model.HourForecast
import com.example.weather.ui.common.BigWeatherBlock
import com.example.weather.ui.formatter.ForecastFormatter
import com.example.weather.ui.formatter.rememberForecastFormatter
import com.example.weather.ui.theme.WeatherAppTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun DayForecastScreenRoute(
    date: LocalDate?
) {
    if (date == null) {
        DayForecastScreen(date = null, uiState = DayForecastUiState())
        return
    }

    val viewModel: DayForecastViewModel = koinViewModel(
        parameters = { parametersOf(date) }
    )
    val uiState = viewModel.uiState.collectAsState().value
    DayForecastScreen(
        date = date,
        uiState = uiState,
        onRetry = viewModel::loadForecast
    )

}

@Composable
fun DayForecastScreen(
    date: LocalDate?,
    uiState: DayForecastUiState,
    onRetry: () -> Unit = {}
) {
    if (date == null) {
        DayForecastStatusScreen(
            title = stringResource(R.string.forecast_invalid_date_title),
            message = stringResource(R.string.forecast_invalid_date_description),
            showErrorIcon = true
        )
    } else {
        if (uiState.isLoading) {
            DayForecastStatusScreen(
                title = stringResource(R.string.forecast_loading),
                message = stringResource(R.string.forecast_loading_description),
                isLoading = true
            )
        } else if (uiState.errorMessage != null) {
            DayForecastStatusScreen(
                title = stringResource(R.string.forecast_invalid_date_title),
                message = uiState.errorMessage,
                onRetry = onRetry
            )
        } else {
            val dayForecast = uiState.dayForecast
            if (dayForecast == null) {
                DayForecastStatusScreen(
                    title = stringResource(R.string.forecast_empty),
                    message = stringResource(R.string.forecast_empty_description),
                    onRetry = onRetry
                )
            } else {
                DayForecastContent(dayForecast = dayForecast)
            }
        }
    }
}

@Composable
private fun DayForecastContent(dayForecast: DayForecast) {
    val formatter = rememberForecastFormatter()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(key = "day", contentType = "summary") {
            BigWeatherBlock(dayForecast = dayForecast)
        }
        item(key = "hourly_title", contentType = "heading") {
            Text(
                text = stringResource(R.string.forecast_hourly_title),
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 4.dp)
                    .semantics { heading() },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        if (dayForecast.hourlyForecast.isEmpty()) {
            item(key = "hourly_empty", contentType = "empty") {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.forecast_hourly_empty),
                        modifier = Modifier.padding(20.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        items(
            items = dayForecast.hourlyForecast,
            contentType = { "hour" }
        ) { forecast ->
            HourForecastRow(forecast = forecast, formatter = formatter)
        }
    }
}

@Composable
private fun HourForecastRow(
    forecast: HourForecast,
    formatter: ForecastFormatter
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = formatter.timeToString(forecast.time),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = forecast.weatherCondition,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = formatter.temperatureToString(forecast.temperature),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun DayForecastStatusScreen(
    title: String,
    message: String,
    isLoading: Boolean = false,
    showErrorIcon: Boolean = false,
    onRetry: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Surface(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .semantics { liveRegion = LiveRegionMode.Polite },
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (showErrorIcon) {
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.error_24),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                if (isLoading) {
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.outlineVariant,
                            strokeWidth = 3.dp
                        )
                    }
                } else if (onRetry != null) {
                    OutlinedIconButton(
                        onClick = onRetry,
                        modifier = Modifier.size(48.dp),
                        colors = IconButtonDefaults.outlinedIconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.refresh_24),
                            contentDescription = stringResource(R.string.forecast_retry)
                        )
                    }
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DayForecastScreenPreview() {
    WeatherAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            DayForecastScreen(
                date = LocalDate.of(2026, 10, 27),
                uiState = DayForecastUiState(
                    dayForecast = DayForecast(
                        date = LocalDate.of(2026, 10, 27),
                        temperature = 15.0,
                        weatherCondition = "Sunny",
                        hourlyForecast = List(24) { hour ->
                            HourForecast(
                                time = LocalTime.of(hour, 0),
                                temperature = 15.0 - kotlin.math.abs(hour - 14) * 0.5,
                                weatherCondition = if (hour in 8..18) "Sunny" else "Clear"
                            )
                        }
                    ),
                    isLoading = false,
                    errorMessage = null
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InvalidForecastDateScreenPreview() {
    WeatherAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            DayForecastScreen(date = null, uiState = DayForecastUiState())
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoadingDayForecastScreenPreview() {
    WeatherAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            DayForecastScreen(
                date = LocalDate.of(2026, 10, 27),
                uiState = DayForecastUiState(isLoading = true)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun DayForecastEmptyScreenPreview() {
    WeatherAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            DayForecastScreen(
                date = LocalDate.of(2026, 10, 27),
                uiState = DayForecastUiState(
                    dayForecast = null,
                    isLoading = false,
                    errorMessage = null
                )
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun DayForecastErrorScreenPreview() {
    WeatherAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            DayForecastScreen(
                date = LocalDate.of(2026, 10, 27),
                uiState = DayForecastUiState(
                    isLoading = false,
                    errorMessage = stringResource(R.string.forecast_error)
                )
            )
        }
    }
}
