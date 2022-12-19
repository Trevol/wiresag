package com.example.wiresag.viewModel

import android.content.Context
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.preference.PreferenceManager
import com.example.wiresag.camera.PhotoRequest
import com.example.wiresag.mapView.CenteredOverlayItem
import com.example.wiresag.mapView.PylonOverlayItem
import com.example.wiresag.mapView.WireSagMap
import com.example.wiresag.mapView.WireSagMapView
import com.example.wiresag.state.WireSpanPhoto
import com.example.wiresag.state.PhotoWithGeoPoint
import com.example.wiresag.osmdroid.toGeoPoint
import com.example.wiresag.state.GeoObjects
import com.example.wiresag.ui.image.annotation.WireSagAnnotationTool
import com.example.wiresag.utils.DMS
import com.example.wiresag.utils.addItem
import com.example.wiresag.utils.prettyFormat
import com.example.wiresag.utils.round
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider

class WireSagViewModel(
    applicationContext: Context,
    private val locationProvider: IMyLocationProvider,
    private val photoRequest: PhotoRequest
) {
    private var currentLocation by mutableStateOf(null as Location?)
    private var prevLocation: Location? = null
    private val geoObjects = GeoObjects()
    private var photoForAnnotation by mutableStateOf(null as WireSpanPhoto?)

    init {
        // Map not working without this line of code
        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        enableMyLocation()
        updateMyLocation(locationProvider.lastKnownLocation)
    }

    fun enableMyLocation() {
        locationProvider.startLocationProvider { location, _ -> updateMyLocation(location) }
    }

    fun disableMyLocation() {
        locationProvider.stopLocationProvider()
    }

    private fun updateMyLocation(newLocation: Location?) {
        prevLocation = currentLocation
        currentLocation = newLocation
    }

    private fun updateMapView(map: WireSagMapView) {

        map.overlay.location.setLocation(currentLocation?.let { GeoPoint(it) })

        val locationIsInitial = currentLocation != null && prevLocation == null
        if (locationIsInitial) {
            map.controller.animateTo(GeoPoint(currentLocation!!), 15.0, null)
            prevLocation = currentLocation // animate only once!!!
        }

        val pylonsOnLayer = map.overlay.pylons.items.map { it.pylon }
        if (geoObjects.pylons.toList() != pylonsOnLayer) {
            map.overlay.pylons.removeAllItems()
            map.overlay.pylons.addItems(
                geoObjects.pylons.map { PylonOverlayItem(it) }
            )
        }

        val photoPlacesOnLayer = map.overlay.photoPoints.items.map { it.point }
        if (geoObjects.placesForPhoto.toList() != photoPlacesOnLayer) {
            map.overlay.photoPoints.removeAllItems()
            map.overlay.photoPoints.addItems(
                geoObjects.placesForPhoto.map { CenteredOverlayItem(geoPoint = it) }
            )
        }

        map.postInvalidate()
    }

    private fun markPylon() {
        currentLocation?.run {
            geoObjects.markPylon(toGeoPoint())
        }
    }

    private val maxDistanceFromPhotoToSpan = 330.0

    private fun takePhotoWithLocation() {
        currentLocation ?: return
        photoRequest.takePhoto { photo ->
            val photo = photo ?: return@takePhoto
            val currentGeoPoint = currentLocation?.toGeoPoint() ?: return@takePhoto
            val span = geoObjects.nearestWireSpan(currentGeoPoint, maxDistanceFromPhotoToSpan) ?: return@takePhoto
            photoForAnnotation = span.photos.addItem(WireSpanPhoto(span, PhotoWithGeoPoint(photo, currentGeoPoint)))
        }
    }

    @Composable
    fun View() {
        Column(modifier = Modifier.fillMaxSize()) {
            if (photoForAnnotation != null) {
                WireSagAnnotationTool(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(2f),
                    spanPhoto = photoForAnnotation!!,
                    onClose = { photoForAnnotation = null }
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .zIndex(1f),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Button(onClick = { markPylon() }, enabled = currentLocation != null) {
                        Text("О")
                    }
                    Button(
                        onClick = { takePhotoWithLocation() },
                        enabled = currentLocation != null && geoObjects.pylons.size > 1
                    ) {
                        Text("Ф")
                    }
                }

                WireSagMap(
                    modifier = Modifier.fillMaxSize(),
                    onInitMapView = {
                        it.controller.setZoom(15.0)
                    },
                    onUpdateMapView = ::updateMapView
                )


            }
            Box() {
                LocationInfo(currentLocation)
            }
        }

    }
}

@Composable
private fun LocationInfo(loc: Location?) {
    Row {
        Text("Лок-я: ")
        if (loc == null) {
            Text("определяется...")
        } else {
            Text(loc.info())
        }
    }
}

private fun Location.info(): String {
    val speedInfo = if (hasSpeed()) speed.round(3) else ""
    return "$provider ${DMS(latitude).prettyFormat()} ${DMS(longitude).prettyFormat()} ${
        accuracy.round(
            1
        )
    } $speedInfo"
}