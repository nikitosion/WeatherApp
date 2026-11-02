package com.example.weather.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyForecastDto(
    val time: List<String>,
    @SerialName("temperature_2m_max")
    val maxTemperatures: List<Double?>,
    @SerialName("temperature_2m_min")
    val minTemperatures: List<Double?>,
    @SerialName("weather_code")
    val weatherCodes: List<Int?>
)