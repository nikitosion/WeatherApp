package com.example.weather.data.repository

import com.example.weather.data.mapper.ForecastMapper
import com.example.weather.data.mapper.InvalidForecastDataException
import com.example.weather.data.remote.WeatherApi
import com.example.weather.domain.ForecastError
import com.example.weather.domain.ForecastResult
import com.example.weather.domain.model.DayForecast
import com.example.weather.domain.repository.WeatherRepository
import okio.IOException
import retrofit2.HttpException
import java.time.LocalDate

class WeatherRepositoryImpl(
    private val apiService: WeatherApi
) : WeatherRepository {
    override suspend fun getForecastForWeek(
        latitude: Double,
        longitude: Double
    ): ForecastResult<List<DayForecast>> {
        try {
            val dto = apiService.getForecast(latitude, longitude)
            val domainForecast = ForecastMapper.dtoToDomain(dto)

            return ForecastResult.Success(domainForecast)

        } catch (ioException: IOException) {
            return ForecastResult.Failure(ForecastError.NETWORK)
        } catch (httpException: HttpException) {
            return ForecastResult.Failure(ForecastError.SERVICE)
        } catch (invalidForecastData: InvalidForecastDataException) {
            return ForecastResult.Failure(ForecastError.INVALID_DATA)
        } catch (illegalArg: IllegalArgumentException) {
            return ForecastResult.Failure(ForecastError.INVALID_DATA)
        } catch (exception: Exception) {
            return ForecastResult.Failure(ForecastError.UNKNOWN)
        }
    }

    override suspend fun getForecastForDay(
        latitude: Double,
        longitude: Double,
        date: LocalDate
    ): ForecastResult<DayForecast?> {
        try {
            val dto = apiService.getForecast(latitude, longitude)

            return ForecastResult.Success(
                ForecastMapper.dtoToDomain(dto).firstOrNull { it.date == date }
            )
        } catch (ioException: IOException) {
            return ForecastResult.Failure(ForecastError.NETWORK)
        } catch (httpException: HttpException) {
            return ForecastResult.Failure(ForecastError.SERVICE)
        } catch (invalidForecastData: InvalidForecastDataException) {
            return ForecastResult.Failure(ForecastError.INVALID_DATA)
        } catch (illegalArg: IllegalArgumentException) {
            return ForecastResult.Failure(ForecastError.INVALID_DATA)
        } catch (exception: Exception) {
            return ForecastResult.Failure(ForecastError.UNKNOWN)
        }
    }

}
