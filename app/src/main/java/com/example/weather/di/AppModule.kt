package com.example.weather.di

import com.example.weather.data.MockRepositoryImpl
import com.example.weather.domain.repository.WeatherRepository
import com.example.weather.ui.dasboard.DashboardViewModel
import com.example.weather.ui.day_forecast.DayForecastViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import java.time.LocalDate

val appModule = module {

    single<WeatherRepository> {
        MockRepositoryImpl()
    }

    viewModel {
        DashboardViewModel(
            weatherRepository = get()
        )
    }

    viewModel { parameters ->
        DayForecastViewModel(
            date = parameters.get<LocalDate>(),
            weatherRepository = get()
        )
    }
}