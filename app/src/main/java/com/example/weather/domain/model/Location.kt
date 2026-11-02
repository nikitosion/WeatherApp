package com.example.weather.domain.model

data class Location(
    val displayName: String?,
    val coordinates: Coordinates,
    val detectedTimeMillis: Long?
)
