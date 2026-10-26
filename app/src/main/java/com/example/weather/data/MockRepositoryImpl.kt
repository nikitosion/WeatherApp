package com.example.weather.data

import com.example.weather.domain.model.DayForecast
import com.example.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.delay
import java.lang.Math.random
import java.time.LocalDate
import kotlin.time.Duration.Companion.milliseconds

class MockRepositoryImpl : WeatherRepository {
    override suspend fun getForecast(
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

}