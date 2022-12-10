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
import androidx.preference.PreferenceManager
import com.example.wiresag.mapView.CenteredOverlayItem
import com.example.wiresag.mapView.PylonOverlayItem
import com.example.wiresag.mapView.WireSagMap
import com.example.wiresag.mapView.WireSagMapView
import com.example.wiresag.utils.*
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider

class WireSagViewModel(
    applicationContext: Context,
    private val locationProvider: IMyLocationProvider
) {
    private var currentLocation by mutableStateOf(null as Location?)
    private var prevLocation: Location? = null

    val geoObjects = GeoObjectsViewModel()

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
        map.locationOverlay.setLocation(currentLocation?.let { GeoPoint(it) })

        val locationIsInitial = currentLocation != null && prevLocation == null
        if (locationIsInitial) {
            map.controller.animateTo(GeoPoint(currentLocation!!), 15.0, null)
            prevLocation = currentLocation // animate only once!!!
        }

        val pylonsOnLayer = map.pylonsOverlay.items.map { it.pylon }
        if (geoObjects.pylons.toList() != pylonsOnLayer) {
            map.pylonsOverlay.removeAllItems()
            map.pylonsOverlay.addItems(
                geoObjects.pylons.map { PylonOverlayItem(it) }
            )
        }

        val photoPlacesOnLayer = map.photoPointsOverlay.items.map { it.point }
        if (geoObjects.photoPlaces.toList() != photoPlacesOnLayer) {
            map.photoPointsOverlay.removeAllItems()
            map.photoPointsOverlay.addItems(
                geoObjects.photoPlaces.map { CenteredOverlayItem(geoPoint = it) }
            )
        }

        map.postInvalidate()
    }

    private fun markPylon() {
        currentLocation?.run {
            geoObjects.markPylon(this)
        }
    }


    @Composable
    fun View() {
        Column(modifier = Modifier.fillMaxSize()) {

            Box(modifier = Modifier.weight(1f)) {
                WireSagMap(
                    modifier = Modifier.fillMaxSize(),
                    onInitMapView = {
                        it.controller.setZoom(15.0)
                    },
                    onUpdateMapView = ::updateMapView
                )
                Row(
                    modifier = Modifier.padding(start = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Button(onClick = { markPylon() }, enabled = currentLocation != null) {
                        Text("О")
                    }
                    Button(onClick = { /*takePhoto()*/ }, enabled = currentLocation != null) {
                        Text("Ф")
                    }
                }

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