package com.example.weather.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [SelectedLocationEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun selectedLocationDao(): SelectedLocationDao
}