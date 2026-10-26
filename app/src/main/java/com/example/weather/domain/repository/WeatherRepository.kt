package com.example.weather.domain.repository

import com.example.weather.domain.model.DayForecast

interface WeatherRepository {
    suspend fun getForecast(
        latitude: Double,
        longitude: Double
    ): List<DayForecast>
}