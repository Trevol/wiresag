package com.example.wiresag.osmdroid

import android.location.Location
import com.example.wiresag.location.Location
import kotlinx.coroutines.*
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import kotlin.random.Random

class DebugLocationProvider(
    val latDelta: Double = 0.000006,
    val lonDelta: Double = 0.000007,
    val initialDelay: Long = 5000,
    val initialLocation: Location? = dummyLocation(),
    val locationUpdateTime: Long = 1000
) : IMyLocationProvider {

    private var lastLocation = initialLocation
    private var job: Job? = null

    override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer?): Boolean {
        stopLocationProvider()
        job = CoroutineScope(Dispatchers.IO).launch {
            delay(initialDelay)
            while (true) {
                val newLocation = lastLocation?.let {
                    Location(it.latitude + latDelta, it.longitude + lonDelta)
                        .apply {
                            accuracy = 111.1f + Random.nextFloat()
                        }
                } ?: dummyLocation()
                lastLocation = newLocation
                withContext(Dispatchers.Main) {
                    myLocationConsumer?.onLocationChanged(lastLocation, this@DebugLocationProvider)
                }
                delay(locationUpdateTime)
            }
        }
        return true
    }

    override fun stopLocationProvider() {
        job?.cancel()
        job = null
        lastLocation = initialLocation
    }

    override fun getLastKnownLocation(): Location? {
        return lastLocation
    }

    override fun destroy() {
        stopLocationProvider()
    }
}

fun dummyLocation() = Location(45.017983, 35.380281)