package com.example.weather.data.local

import com.example.weather.domain.model.Coordinates

interface CityGeocoder {
    suspend fun getCityName(coordinates: Coordinates): String?
}
