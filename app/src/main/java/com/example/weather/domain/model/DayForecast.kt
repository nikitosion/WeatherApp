package com.example.weather.domain.model

import java.time.LocalDate

data class DayForecast(
    val date: LocalDate,
    val temperature: Double,
    val weatherCondition: String
)
