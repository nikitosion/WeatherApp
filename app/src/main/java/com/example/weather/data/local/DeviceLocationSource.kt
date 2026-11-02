package com.example.weather.data.local

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.example.weather.domain.model.Coordinates
import com.example.weather.domain.model.Location
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import okio.IOException

class DeviceLocationSource(
    context: Context,
    private val locationClient: FusedLocationProviderClient,
    private val cityGeocoder: CityGeocoder
) {
    private val appContext = context.applicationContext

    suspend fun detectLocation(): Location {
        val locationManager = appContext.getSystemService(
            LocationManager::class.java
        )

        if (!LocationManagerCompat.isLocationEnabled(locationManager)) {
            throw IOException("Location services are disabled")
        }

        val hasPrecisePermission = ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPrecisePermission) {
            throw SecurityException("Precise location permission is required")
        }

        val request = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMaxUpdateAgeMillis(0)
            .setDurationMillis(20_000)
            .build()

        val cancellation = CancellationTokenSource()

        val deviceLocation = try {
            locationClient.getCurrentLocation(
                request,
                cancellation.token
            ).await()
        } finally {
            cancellation.cancel()
        } ?: throw IOException("Failed to retrieve device location")

        val coordinates = Coordinates(
            latitude = deviceLocation.latitude,
            longitude = deviceLocation.longitude
        )

        return Location(
            coordinates = coordinates,
            displayName = cityGeocoder.getCityName(coordinates),
            detectedTimeMillis = deviceLocation.time
        )
    }

}
