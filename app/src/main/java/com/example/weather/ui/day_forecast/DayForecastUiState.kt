package com.example.weather.ui.day_forecast

import com.example.weather.domain.ForecastError
import com.example.weather.domain.model.DayForecast
import com.example.weather.domain.model.Location

data class DayForecastUiState(
    val dayForecast: DayForecast? = null,
    val isLoading: Boolean = true,
    val location: Location? = null,
    val error: ForecastError? = null
)
