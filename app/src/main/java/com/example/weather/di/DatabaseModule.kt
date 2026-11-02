package com.example.weather.di

import androidx.room3.Room
import com.example.weather.data.local.AppDatabase
import org.koin.dsl.module

val databaseModule = module {

    single<AppDatabase> {
        Room.databaseBuilder<AppDatabase>(
            context = get(),
            name = "weather_database"
        ).build()
    }
}