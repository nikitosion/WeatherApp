package com.example.weather.ui.choose_location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.domain.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class ChooseLocationViewModel(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChooseLocationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            locationRepository.observeSelectedLocation().collect { location ->
                _uiState.update { it.copy(location = location) }
            }
        }
    }

    fun onPermissionsChanged(fineGranted: Boolean) {
        _uiState.update {
            it.copy(
                hasPreciseLocationPermission = fineGranted
            )
        }
    }

    fun detectLocation() {
        if (_uiState.value.isLoading) return
        if (!_uiState.value.hasPreciseLocationPermission) {
            onPermissionDenied()
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )
        viewModelScope.launch {
            try {
                val location = locationRepository.detectLocation()
                locationRepository.save(location)
                _uiState.update { it.copy(isLoading = false, location = location) }
            } catch (_: SecurityException) {
                onPermissionDenied()
            } catch (exception: IOException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Unable to determine your location"
                    )
                }
            }
        }
    }

    fun onPermissionDenied() {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = "Allow precise location access to find your current city.",
            hasPreciseLocationPermission = false
        )
    }
}
