package com.example.weather.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyForecastDto(
    val time: List<String>,
    @SerialName("temperature_2m")
    val temperatures: List<Double?>,
    @SerialName("weather_code")
    val weatherCodes: List<Int?>
)