package com.example.weather.data.repository

import com.example.weather.data.ForecastMapper
import com.example.weather.data.remote.WeatherApi
import com.example.weather.domain.model.DayForecast
import com.example.weather.domain.repository.WeatherRepository
import java.time.LocalDate

class WeatherRepositoryImpl(
    private val apiService: WeatherApi
) : WeatherRepository {
    override suspend fun getForecastForWeek(
        latitude: Double,
        longitude: Double
    ): List<DayForecast>? {
        val dto = apiService.getForecast(latitude, longitude)

        return ForecastMapper.dtoToDomain(dto)
    }

    override suspend fun getForecastForDay(
        latitude: Double,
        longitude: Double,
        date: LocalDate
    ): DayForecast? {
        val dto = apiService.getForecast(latitude, longitude)

        return ForecastMapper.dtoToDomain(dto).firstOrNull { it.date == date }
    }

}
