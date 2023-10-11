package com.example.wiresag.osmdroid

import android.location.Location
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider

class StubLocationProvider : IMyLocationProvider {
    override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer?) = true
    override fun stopLocationProvider() = Unit
    override fun getLastKnownLocation() = dummyLocation()
    override fun destroy() = Unit
}