package com.example.weather.data.repository

import com.example.weather.data.local.AppDatabase
import com.example.weather.data.local.DeviceLocationSource
import com.example.weather.data.mapper.LocationMapper
import com.example.weather.domain.model.Location
import com.example.weather.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val deviceLocationSource: DeviceLocationSource
) : LocationRepository {
    override fun observeSelectedLocation(): Flow<Location?> {
        return appDatabase.selectedLocationDao().observeSelectedLocation()
            .map { entity ->
                if (entity == null) {
                    null
                } else {
                    LocationMapper.entityToDomain(entity)
                }
            }
    }

    override suspend fun detectLocation(): Location {
        return deviceLocationSource.detectLocation()
    }

    override suspend fun save(location: Location) {
        appDatabase.selectedLocationDao().insertSelectedLocation(
            LocationMapper.domainToEntity(location)
        )
    }

    override suspend fun clear() {
        appDatabase.selectedLocationDao().deleteSelectedLocation()
    }
}