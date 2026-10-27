package com.example.weather.domain.repository

import com.example.weather.domain.model.DayForecast
import java.time.LocalDate

interface WeatherRepository {
    suspend fun getForecastForWeek(
        latitude: Double,
        longitude: Double
    ): List<DayForecast>

    suspend fun getForecastForDay(
        latitude: Double,
        longitude: Double,
        date: LocalDate
    ): DayForecast
}