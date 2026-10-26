package com.example.weather.ui.dasboard

import androidx.lifecycle.ViewModel
import com.example.weather.domain.repository.WeatherRepository

class DashboardViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
}