package com.example.weather.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponseDto(
    val timezone: String,
    val daily: DailyForecastDto,
    val hourly: HourlyForecastDto
)
