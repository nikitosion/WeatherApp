package com.example.weather.di

import com.example.weather.data.repository.LocationRepositoryImpl
import com.example.weather.data.repository.WeatherRepositoryImpl
import com.example.weather.domain.repository.LocationRepository
import com.example.weather.domain.repository.WeatherRepository
import com.example.weather.ui.dasboard.DashboardViewModel
import com.example.weather.ui.day_forecast.DayForecastViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import java.time.LocalDate

val appModule = module {

    single<WeatherRepository> {
        WeatherRepositoryImpl(
            apiService = get()
        )
    }

    single<LocationRepository> {
        LocationRepositoryImpl(
            appDatabase = get()
        )
    }

    viewModel {
        DashboardViewModel(
            weatherRepository = get(),
            locationRepository = get()
        )
    }

    viewModel { parameters ->
        DayForecastViewModel(
            date = parameters.get<LocalDate>(),
            weatherRepository = get(),
            locationRepository = get()
        )
    }
}
