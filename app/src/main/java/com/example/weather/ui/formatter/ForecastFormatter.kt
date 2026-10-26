package com.example.weather.ui.formatter

import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class ForecastFormatter(
    locale: Locale
) {
    private val dateFormatter = DateTimeFormatter.ofPattern(
        DateFormat.getBestDateTimePattern(locale, "EEEEdMMMM"),
        locale
    )

    fun dateToString(date: LocalDate): String {
        return date.format(dateFormatter).uppercase()
    }

    fun temperatureToString(temperature: Double): String {
        val eps = 1e-9
        val intTemperature = temperature.toInt()

        return if (temperature - eps > 0.0 || intTemperature == 0) {
            "+${intTemperature}°"
        } else {
            "${intTemperature}°"
        }
    }
}

@Composable
fun rememberForecastFormatter(): ForecastFormatter {
    val locale = LocalConfiguration.current.locales[0]

    return remember(locale) {
        ForecastFormatter(locale)
    }
}