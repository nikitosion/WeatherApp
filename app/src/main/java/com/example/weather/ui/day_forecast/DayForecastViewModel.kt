package com.example.weather.ui.day_forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.domain.ForecastResult
import com.example.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class DayForecastViewModel(
    private val date: LocalDate,
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DayForecastUiState(isLoading = false))
    val uiState = _uiState.asStateFlow()

    init {
        loadForecast()
    }

    fun loadForecast() {
        if (_uiState.value.isLoading) return

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val forecast = weatherRepository.getForecastForDay(
                latitude = 55.7512,
                longitude = 37.6184,
                date = date
            )

            when (forecast) {
                is ForecastResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            dayForecast = forecast.data,
                            error = null
                        )
                    }
                }

                is ForecastResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            dayForecast = null,
                            error = forecast.error
                        )
                    }
                }
            }
        }
    }
}
