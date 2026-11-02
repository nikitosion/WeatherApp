package com.example.weather.data.mapper

import com.example.weather.data.local.SelectedLocationEntity
import com.example.weather.domain.model.Coordinates
import com.example.weather.domain.model.Location

object LocationMapper {

    fun entityToDomain(entity: SelectedLocationEntity): Location {
        return Location(
            displayName = entity.displayName,
            coordinates = Coordinates(
                latitude = entity.latitude,
                longitude = entity.longitude
            ),
            detectedTimeMillis = entity.detectedTimeMillis
        )
    }

    fun domainToEntity(location: Location): SelectedLocationEntity {
        return SelectedLocationEntity(
            latitude = location.coordinates.latitude,
            longitude = location.coordinates.longitude,
            displayName = location.displayName,
            detectedTimeMillis = location.detectedTimeMillis
        )
    }
}