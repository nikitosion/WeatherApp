package com.example.weather.ui.day_forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.CancellationException
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

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val forecast = weatherRepository.getForecastForDay(
                    latitude = 55.7512,
                    longitude = 37.6184,
                    date = date
                )
                _uiState.update {
                    it.copy(dayForecast = forecast, isLoading = false)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Please try again in a moment."
                    )
                }
            }
        }
    }
}
