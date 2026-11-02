package com.example.weather.data.local

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectedLocationDao {
    @Query("SELECT * FROM selected_location WHERE id = 1")
    fun observeSelectedLocation(): Flow<SelectedLocationEntity?>

    @Upsert
    suspend fun insertSelectedLocation(location: SelectedLocationEntity)

    @Query("DELETE FROM selected_location WHERE id = 1")
    suspend fun deleteSelectedLocation()
}
