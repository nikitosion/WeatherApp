package com.example.weather.data

import com.example.weather.data.remote.ForecastResponseDto
import com.example.weather.domain.model.DayForecast
import com.example.weather.domain.model.HourForecast
import java.time.LocalDate
import java.time.LocalDateTime

object ForecastMapper {

    fun dtoToDomain(dto: ForecastResponseDto): List<DayForecast> {
        val daily = dto.daily
        val hourly = dto.hourly

        val datedHours = hourly.time.indices.map { index ->
            val dateTime = LocalDateTime.parse(hourly.time[index])
            val temperature = requireNotNull(hourly.temperatures[index]) {
                "Temperature data is missing for hour at index $index"
            }

            dateTime.toLocalDate() to HourForecast(
                time = dateTime.toLocalTime(),
                temperature = temperature,
                weatherCondition = weatherCodeToCondition(
                    hourly.weatherCodes[index]
                )
            )
        }

        val hoursByDate = datedHours.groupBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        )

        return daily.time.indices.map { index ->
            val date = LocalDate.parse(daily.time[index])

            val temperature = requireNotNull(daily.maxTemperatures[index]) {
                "Temperature data is missing for day at index $index"
            }

            DayForecast(
                date = date,
                temperature = temperature,
                weatherCondition = weatherCodeToCondition(
                    daily.weatherCodes[index]
                ),
                hourlyForecast = hoursByDate[date]
                    .orEmpty()
                    .sortedBy { it.time }
            )
        }.sortedBy { it.date }
    }

    private fun weatherCodeToCondition(code: Int?): String =
        when (code) {
            0 -> "Clear"
            1 -> "Mainly clear"
            2 -> "Partly cloudy"
            3 -> "Overcast"
            45, 48 -> "Fog"
            51, 53, 55 -> "Drizzle"
            56, 57 -> "Freezing drizzle"
            61, 63, 65 -> "Rain"
            66, 67 -> "Freezing rain"
            71, 73, 75, 77 -> "Snow"
            80, 81, 82 -> "Rain showers"
            85, 86 -> "Snow showers"
            95, 97 -> "Thunderstorm"
            96, 99 -> "Thunderstorm with hail"
            else -> "Unknown"
        }
}