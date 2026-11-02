package com.example.weather.data.local

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(
    tableName = "selected_location"
)
data class SelectedLocationEntity(
    @PrimaryKey val id: Int = 1,
    val latitude: Double,
    val longitude: Double,
    @ColumnInfo("display_name") val displayName: String?,
    @ColumnInfo("detected_time_millis") val detectedTimeMillis: Long?
)
