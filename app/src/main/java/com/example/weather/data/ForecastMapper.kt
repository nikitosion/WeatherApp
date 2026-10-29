package com.example.weather.data

import com.example.weather.data.remote.ForecastResponseDto
import com.example.weather.domain.model.DayForecast
import com.example.weather.domain.model.HourForecast
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class InvalidForecastDataException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)

private fun <T : Any> requireForecastValue(
    value: T?,
): T {
    return value ?: throw InvalidForecastDataException(
        "Required forecast value is missing"
    )
}


private fun requireParseDate(
    value: String,
): LocalDate = requireParsedValue(
    value = value,
    parser = LocalDate::parse,
)

private fun requireParseDateTime(
    value: String,
): LocalDateTime = requireParsedValue(
    value = value,
    parser = LocalDateTime::parse,
)

private fun <T> requireParsedValue(
    value: String,
    parser: (String) -> T,
): T {
    return try {
        parser(value)
    } catch (e: DateTimeParseException) {
        throw InvalidForecastDataException(
            message = "Failed to parse forecast value: $value",
            cause = e,
        )
    }
}

object ForecastMapper {

    fun dtoToDomain(dto: ForecastResponseDto): List<DayForecast> {
        val daily = dto.daily
        val hourly = dto.hourly

        val datedHours = hourly.time.indices.map { index ->
            val dateTime = requireParseDateTime(hourly.time[index])
            val temperature = requireForecastValue(hourly.temperatures[index])

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
            val date = requireParseDate(daily.time[index])

            val temperature = requireForecastValue(daily.maxTemperatures[index])

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