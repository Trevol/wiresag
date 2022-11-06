package com.example.wiresag.osmdroid

import android.location.Location
import kotlinx.coroutines.*
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import kotlin.random.Random

class DummyLocationProvider(
    val latDelta: Double = 0.0003,
    val lonDelta: Double = 0.0001,
    val initialDelay: Long = 5000,
    val initialLocation: Location? = dummyLocation()
) : IMyLocationProvider {

    private var lastLocation = initialLocation
    private var job: Job? = null

    override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer?): Boolean {
        stopLocationProvider()
        job = GlobalScope.launch(Dispatchers.IO) {
            delay(initialDelay)
            while (true) {
                val newLocation = lastLocation?.let {
                    Location("dummy").apply {
                        latitude = it.latitude + latDelta
                        longitude = it.longitude + lonDelta
                        accuracy = 111.1f + Random.nextFloat()
                    }
                } ?: dummyLocation()
                lastLocation = newLocation
                withContext(Dispatchers.Main){
                    myLocationConsumer?.onLocationChanged(lastLocation, this@DummyLocationProvider)
                }
                delay(1000)
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

    companion object {
        fun dummyLocation() = Location("dummy").apply {
            latitude = 45.017983
            longitude = 35.380281
            accuracy = 111.1f
        }
    }
}