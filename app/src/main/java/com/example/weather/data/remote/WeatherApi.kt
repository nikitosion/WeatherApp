package com.example.weather.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("daily") daily: String =
            "temperature_2m_max,temperature_2m_min,weather_code",
        @Query("hourly") hourly: String =
            "temperature_2m,weather_code",
        @Query("timezone") timezone: String = "auto",
        @Query("forecast_days") forecastDays: Int = 7,
    ): ForecastResponseDto
}