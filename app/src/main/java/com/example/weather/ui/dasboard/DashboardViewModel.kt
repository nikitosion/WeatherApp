package com.example.weather.ui.dasboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadForecast(55.7512, 37.6184)
    }

    fun loadForecast(latitude: Double, longitude: Double) {
        if (_uiState.value.isLoading) return

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val forecast = weatherRepository.getForecast(latitude, longitude)
                _uiState.update {
                    it.copy(weekForecast = forecast, errorMessage = null)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Please try again in a moment."
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                    )
                }
            }
        }
    }
}
