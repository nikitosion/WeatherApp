package com.example.weather.ui.day_forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.domain.ForecastResult
import com.example.weather.domain.repository.LocationRepository
import com.example.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class DayForecastViewModel(
    private val date: LocalDate,
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DayForecastUiState(isLoading = false))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val location = locationRepository.observeSelectedLocation().first()

            if (location == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        location = null
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    location = location
                )
            }

            loadForecast(
                longitude = location.coordinates.longitude,
                latitude = location.coordinates.latitude
            )
        }


    }

    fun retryLoadForecast() {
        val location = _uiState.value.location ?: return
        viewModelScope.launch {
            loadForecast(
                longitude = location.coordinates.longitude,
                latitude = location.coordinates.latitude
            )
        }
    }

    private suspend fun loadForecast(longitude: Double, latitude: Double) {
        if (_uiState.value.isLoading) return

        _uiState.update { it.copy(isLoading = true, error = null) }

        val forecast = weatherRepository.getForecastForDay(
            latitude = latitude,
            longitude = longitude,
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
