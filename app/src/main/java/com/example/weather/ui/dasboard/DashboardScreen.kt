package com.example.weather.ui.dasboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weather.R
import com.example.weather.domain.model.DayForecast
import com.example.weather.ui.theme.WeatherAppTheme
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@Composable
fun DashboardScreenRoute(viewModel: DashboardViewModel = koinViewModel()) {
    DashboardScreen(viewModel)
}

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel
) {
    DashboardScreenContent()
}

@Composable
fun DashboardScreenContent(
    weekForecast: List<DayForecast>? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Moscow",
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

        if (weekForecast.isNullOrEmpty()) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedIconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.refresh_24),
                        contentDescription = "Refresh Icon"
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    text = "No forecast available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            val todayForecast = weekForecast.first()

            TodayWeatherBlock(
                modifier = Modifier.padding(vertical = 16.dp),
                todayForecast = todayForecast
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
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (dayForecast in weekForecast) {
                    WeekDayForecast(
                        modifier = Modifier,
                        dayForecast = dayForecast
                    )
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
            weekForecast = weekForecast
        )
    }
}

@Composable
@Preview(showBackground = true)
fun NullDashboardScreenPreview() {
    WeatherAppTheme {
        DashboardScreenContent(
            weekForecast = null
        )
    }
}