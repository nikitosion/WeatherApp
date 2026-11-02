package com.example.weather.di

import com.example.weather.data.local.AndroidCityGeocoder
import com.example.weather.data.local.CityGeocoder
import com.example.weather.data.local.DeviceLocationSource
import com.example.weather.data.repository.LocationRepositoryImpl
import com.example.weather.data.repository.WeatherRepositoryImpl
import com.example.weather.domain.repository.LocationRepository
import com.example.weather.domain.repository.WeatherRepository
import com.example.weather.ui.choose_location.ChooseLocationViewModel
import com.example.weather.ui.dasboard.DashboardViewModel
import com.example.weather.ui.day_forecast.DayForecastViewModel
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
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
            appDatabase = get(),
            deviceLocationSource = get()
        )
    }

    single<CityGeocoder> {
        AndroidCityGeocoder(context = androidContext())
    }

    single {
        DeviceLocationSource(
            context = androidContext(),
            locationClient = LocationServices.getFusedLocationProviderClient(
                androidContext()
            ),
            cityGeocoder = get()
        )
    }

    viewModel {
        DashboardViewModel(
            weatherRepository = get(),
            locationRepository = get()
        )
    }

    viewModel {
        ChooseLocationViewModel(
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
