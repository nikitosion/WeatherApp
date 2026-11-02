package com.example.weather.ui.choose_location

import com.example.weather.domain.model.Location

data class ChooseLocationUiState(
    val isLoading: Boolean = false,
    val error: ChooseLocationError? = null,
    val location: Location? = null,
    val hasPreciseLocationPermission: Boolean = false
)
