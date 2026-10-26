package com.example.weather.di

import com.example.weather.data.MockRepositoryImpl
import com.example.weather.domain.repository.WeatherRepository
import com.example.weather.ui.dasboard.DashboardViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<WeatherRepository> {
        MockRepositoryImpl()
    }

    viewModel {
        DashboardViewModel(
            weatherRepository = get()
        )
    }
}