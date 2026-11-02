package com.example.weather.ui.dasboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weather.R
import com.example.weather.domain.model.DayForecast
import com.example.weather.ui.formatter.rememberForecastFormatter
import com.example.weather.ui.theme.WeatherAppTheme
import java.time.LocalDate

@Composable
fun WeekDayForecast(
    modifier: Modifier = Modifier,
    dayForecast: DayForecast,
    onDayClick: () -> Unit
) {
    val formatter = rememberForecastFormatter()

    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
            .clickable { onDayClick() }
            .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column() {
            Text(
                text = formatter.dateToTitleString(dayForecast.date),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = formatter.temperatureToString(dayForecast.temperature),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        IconButton(
            onClick = onDayClick,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.secondary,
                containerColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Icon(
                painter = androidx.compose.ui.res.painterResource(id = R.drawable.keyboard_arrow_right_24),
                contentDescription = stringResource(R.string.forecast_open_day)
            )
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
fun WeekDayForecastPreview() {
    WeatherAppTheme {
        WeekDayForecast(
            dayForecast = DayForecast(
                date = LocalDate.now(),
                temperature = -5.5,
                weatherCondition = "Cloudy"
            ),
            onDayClick = {}
        )
    }
}
