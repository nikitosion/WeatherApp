package com.example.weather.data.local

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.example.weather.domain.model.Coordinates
import com.example.weather.domain.model.Location
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import okio.IOException
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class DeviceLocationSource(
    context: Context,
    private val locationClient: FusedLocationProviderClient
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
            displayName = getCityName(coordinates),
            detectedTimeMillis = deviceLocation.time
        )
    }

    private suspend fun getCityName(coordinates: Coordinates): String? {
        if (!Geocoder.isPresent()) {
            return null
        }

        val geocoder = Geocoder(appContext, Locale.getDefault())

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                withTimeoutOrNull(5_000.milliseconds) {
                    val result = CompletableDeferred<String?>(
                        parent = coroutineContext[Job]
                    )

                    geocoder.getFromLocation(
                        coordinates.latitude,
                        coordinates.longitude,
                        1,
                        object : Geocoder.GeocodeListener {
                            override fun onGeocode(addresses: MutableList<Address>) {
                                result.complete(addresses.firstOrNull()?.locality)
                            }

                            override fun onError(errorMessage: String?) {
                                result.complete(null)
                            }
                        }
                    )

                    result.await()
                }
            } else {
                withContext(Dispatchers.IO) {
                    geocoder.getFromLocation(
                        coordinates.latitude,
                        coordinates.longitude,
                        1
                    )?.firstOrNull()?.locality
                }
            }
        } catch (e: IOException) {
            null
        }
    }
}
