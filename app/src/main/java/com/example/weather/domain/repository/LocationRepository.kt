package com.example.weather.domain.repository

import com.example.weather.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun observeSelectedLocation(): Flow<Location?>

    suspend fun detectLocation(): Location

    suspend fun save(location: Location)

    suspend fun clear()
}