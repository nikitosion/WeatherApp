package com.example.weather.ui.dasboard

import com.example.weather.domain.ForecastError
import com.example.weather.domain.model.DayForecast
import com.example.weather.domain.model.Location

data class DashboardUiState(
    val isLoading: Boolean = false,
    val weekForecast: List<DayForecast>? = null,
    val location: Location? = null,
    val error: ForecastError? = null
)
