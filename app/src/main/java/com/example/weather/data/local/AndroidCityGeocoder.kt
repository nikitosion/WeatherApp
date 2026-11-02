package com.example.weather.data.local

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.example.weather.domain.model.Coordinates
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class AndroidCityGeocoder(context: Context) : CityGeocoder {
    private val appContext = context.applicationContext

    @Suppress("DEPRECATION")
    override suspend fun getCityName(coordinates: Coordinates): String? {
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
        } catch (_: IOException) {
            null
        }
    }
}
