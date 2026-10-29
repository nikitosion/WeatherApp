package com.example.weather.domain

sealed interface ForecastResult<out T> {
    data class Success<T>(
        val data: T
    ) : ForecastResult<T>

    data class Failure(
        val error: ForecastError
    ) : ForecastResult<Nothing>
}