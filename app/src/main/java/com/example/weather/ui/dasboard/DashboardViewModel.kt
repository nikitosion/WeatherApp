package com.example.weather.ui.dasboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.domain.ForecastResult
import com.example.weather.domain.repository.LocationRepository
import com.example.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {

            locationRepository.observeSelectedLocation()
                .distinctUntilChanged()
                .collectLatest { location ->
                    _uiState.update {
                        it.copy(
                            location = location
                        )
                    }

                    if (location != null) {
                        loadForecastForWeek(
                            latitude = location.coordinates.latitude,
                            longitude = location.coordinates.longitude
                        )
                    }

                }
        }
    }

    fun retryLoadForecast() {
        val location = _uiState.value.location ?: return
        viewModelScope.launch {
            loadForecastForWeek(
                latitude = location.coordinates.latitude,
                longitude = location.coordinates.longitude
            )
        }
    }

    private suspend fun loadForecastForWeek(latitude: Double, longitude: Double) {
        if (_uiState.value.isLoading) return

        _uiState.update { it.copy(isLoading = true, error = null) }
        val forecast = weatherRepository.getForecastForWeek(latitude, longitude)

        when (forecast) {
            is ForecastResult.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        weekForecast = forecast.data,
                        error = null
                    )
                }
            }

            is ForecastResult.Failure -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        weekForecast = emptyList(),
                        error = forecast.error
                    )
                }
            }

        }
    }
}
