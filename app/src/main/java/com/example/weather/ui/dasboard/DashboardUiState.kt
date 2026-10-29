package com.example.weather.ui.dasboard

import com.example.weather.domain.ForecastError
import com.example.weather.domain.model.DayForecast

data class DashboardUiState(
    val isLoading: Boolean = false,
    val weekForecast: List<DayForecast>? = null,
    val error: ForecastError? = null
)
