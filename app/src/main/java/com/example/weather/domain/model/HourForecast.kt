package com.example.weather.domain.model

import java.time.LocalTime

data class HourForecast(
    val time: LocalTime,
    val temperature: Double,
    val weatherCondition: WeatherCondition
)
