package com.example.wiresag.viewModel

import android.location.Location
import androidx.compose.runtime.*
import com.example.wiresag.AppSettings
import com.example.wiresag.camera.PhotoRequest
import com.example.wiresag.location.Location
import com.example.wiresag.mapView.WireSagMapView
import com.example.wiresag.mapView.overlays.MapViewMotionEvent
import com.example.wiresag.mapView.overlays.MapViewMotionEventScope
import com.example.wiresag.osmdroid.StubLocationProvider
import com.example.wiresag.osmdroid.toGeoPoint
import com.example.wiresag.state.ObjectContext
import com.example.wiresag.state.WireSpanGeoMeasurements
import com.example.wiresag.state.WireSpanPhoto
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider

class WireSagViewModel(
    val settings: AppSettings,
    private val locationProvider: IMyLocationProvider,
    private val photoRequest: PhotoRequest,
    val objectContext: ObjectContext,
) {
    var settingsMode by mutableStateOf(false)
    var zoomLevel by mutableStateOf(15.0)
    var providerLocation by mutableStateOf(null as Location?)
    private var isInitialLocation = false
    private var initialLocationAnimated = false
    val currentLocation by derivedStateOf { providerLocation?.toGeoPoint() }

    init {
        enableMyLocation()
        updateMyLocation(locationProvider.lastKnownLocation)
    }

    val nearestSpanMeasurements by derivedStateOf {
        val currentLocation = currentLocation
        if (currentLocation == null || zoomLevel < 18) {
            null
        } else {
            objectContext.nearestWireSpan(currentLocation, maxDistanceFromPhotoToSpan)
                ?.let { span ->
                    WireSpanGeoMeasurements(span, currentLocation)
                }
        }
    }

    var photoForAnnotation by mutableStateOf(null as WireSpanPhoto?)

    fun enableMyLocation() {
        locationProvider.startLocationProvider { location, _ -> updateMyLocation(location) }
    }

    fun disableMyLocation() {
        locationProvider.stopLocationProvider()
    }

    fun clearData() {
        objectContext.spans.clear()
        objectContext.pylons.clear()
        settings.spanImagesDirectory.listFiles()
            ?.forEach { it.delete() }
    }

    private fun updateMyLocation(newLocation: Location?) {
        if (providerLocation == null && newLocation != null) {
            isInitialLocation = true
        }
        providerLocation = newLocation
    }

    fun updateMapView(map: WireSagMapView) {
        if (isInitialLocation && !initialLocationAnimated) {
            map.controller.animateTo(currentLocation!!, 19.5, null)
            initialLocationAnimated = true
        }
    }

    fun markStandalonePylon() {
        currentLocation?.run {
            objectContext.markStandalonePylon(this)
        }
    }

    fun markPylonWithSpan() {
        currentLocation?.run {
            objectContext.markPylonWithSpan(this)
        }
    }

    private val maxDistanceFromPhotoToSpan = 100.0

    fun takePhotoWithLocation() {
        currentLocation ?: return
        photoRequest.takePhoto { photo ->
            val photo = photo ?: return@takePhoto
            val photoLocation = currentLocation ?: return@takePhoto
            val span = objectContext.nearestWireSpan(photoLocation, maxDistanceFromPhotoToSpan)
                ?: return@takePhoto
            photoForAnnotation = objectContext.addWireSpanPhoto(span, photo, photoLocation)
        }
    }

    fun onSingleTapConfirmed(event: MapViewMotionEventScope): Boolean {
        val tappedPhoto = objectContext.nearestSpanPhoto(event.geoPoint, maxDistance = 3.0)
        photoForAnnotation = tappedPhoto
        return tappedPhoto != null
    }

    fun navigateToLocation(location: Location?) {
        if (location != null) {
            //TODO: Navigate!!! But how?
        }
    }

    fun initLongPressHandler(): MapViewMotionEvent? =
        if (locationProvider is StubLocationProvider) {
            {
                providerLocation = it.geoPoint.run { Location(latitude, longitude) }
                true
            }
        } else null
}
