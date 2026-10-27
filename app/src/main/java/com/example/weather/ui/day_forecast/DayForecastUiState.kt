package com.example.weather.ui.day_forecast

import com.example.weather.domain.model.DayForecast

data class DayForecastUiState(
    val dayForecast: DayForecast? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
