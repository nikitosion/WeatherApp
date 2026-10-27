package com.example.weather.data

import com.example.weather.domain.model.DayForecast
import com.example.weather.domain.model.HourForecast
import com.example.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.delay
import java.lang.Math.random
import java.time.LocalDate
import java.time.LocalTime
import kotlin.time.Duration.Companion.milliseconds

class MockRepositoryImpl : WeatherRepository {
    override suspend fun getForecastForWeek(
        latitude: Double,
        longitude: Double
    ): List<DayForecast> {
        val today = LocalDate.now()

        delay((random() * 2000 + 1000).toLong().milliseconds)

        return listOf(
            DayForecast(
                date = today,
                temperature = 10.0,
                weatherCondition = "Sunny"
            ),
            DayForecast(
                date = today.plusDays(1),
                temperature = 8.0,
                weatherCondition = "Cloudy"
            ),
            DayForecast(
                date = today.plusDays(2),
                temperature = 5.0,
                weatherCondition = "Rainy"
            ),
            DayForecast(
                date = today.plusDays(3),
                temperature = 12.0,
                weatherCondition = "Sunny"
            ),
            DayForecast(
                date = today.plusDays(4),
                temperature = 7.0,
                weatherCondition = "Windy"
            ),
            DayForecast(
                date = today.plusDays(5),
                temperature = 9.0,
                weatherCondition = "Snowy"
            ),
            DayForecast(
                date = today.plusDays(6),
                temperature = 11.0,
                weatherCondition = "Foggy"
            ),
        )
    }

    override suspend fun getForecastForDay(
        latitude: Double,
        longitude: Double,
        date: LocalDate
    ): DayForecast {
        delay((random() * 2000 + 1000).toLong().milliseconds)

        return DayForecast(
            date = date,
            temperature = 18.0,
            weatherCondition = "Sunny",
            hourlyForecast = List(24) { hour ->
                HourForecast(
                    time = LocalTime.of(hour, 0),
                    temperature = 18.0 - kotlin.math.abs(hour - 14) * 0.5,
                    weatherCondition = when (hour) {
                        in 0..5, in 21..23 -> "Clear"
                        in 6..9, in 18..20 -> "Cloudy"
                        else -> "Sunny"
                    }
                )
            }
        )
    }

}
