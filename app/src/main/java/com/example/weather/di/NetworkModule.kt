package com.example.weather.di

import com.example.weather.data.remote.WeatherApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single<Json> {
        Json {
            ignoreUnknownKeys = true
        }
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .callTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/v1/")
            .client(get<OkHttpClient>())
            .addConverterFactory(
                get<Json>().asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
    }

    single<WeatherApi> {
        get<Retrofit>().create(WeatherApi::class.java)
    }
}