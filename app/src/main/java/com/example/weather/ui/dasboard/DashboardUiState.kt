package com.example.weather.ui.dasboard

import com.example.weather.domain.model.DayForecast

data class DashboardUiState(
    val isLoading: Boolean = false,
    val weekForecast: List<DayForecast>? = null,
    val errorMessage: String? = null
)
