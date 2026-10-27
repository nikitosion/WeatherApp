package com.example.weather.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weather.domain.model.DayForecast
import com.example.weather.ui.formatter.rememberForecastFormatter
import com.example.weather.ui.theme.WeatherAppTheme
import java.time.LocalDate

@Composable
fun BigWeatherBlock(
    modifier: Modifier = Modifier,
    dayForecast: DayForecast,
    onDayClick: (() -> Unit)? = null
) {
    val formatter = rememberForecastFormatter()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(28.dp)
            )
            .then(
                if (onDayClick != null) Modifier.clickable(onClick = onDayClick)
                else Modifier
            )
            .padding(20.dp)
    ) {
        Text(
            text = formatter.dateToTitleString(dayForecast.date),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = formatter.temperatureToString(dayForecast.temperature),
            style = MaterialTheme.typography.displayLarge.copy(fontSize = MaterialTheme.typography.displayLarge.fontSize * 1.3),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )

        Text(
            text = dayForecast.weatherCondition,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
@Preview
fun BigWeatherBlockPreview() {
    WeatherAppTheme {
        BigWeatherBlock(
            dayForecast = DayForecast(
                date = LocalDate.now(),
                temperature = 25.0,
                weatherCondition = "Cloudy",
            ),
            onDayClick = {}
        )
    }
}
