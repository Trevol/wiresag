package com.example.wiresag

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toDrawable
import androidx.preference.PreferenceManager
import com.example.wiresag.osmdroid.SimpleLocationOverlay2
import com.example.wiresag.osmdroid.compose.MapView
import com.example.wiresag.utils.DMS
import com.example.wiresag.utils.prettyFormat
import com.example.wiresag.utils.round
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.mylocation.SimpleLocationOverlay

class WireSagViewModel(
    applicationContext: Context,
    private val locationProvider: IMyLocationProvider
) : IMyLocationConsumer {
    private var mapInstance: MapView? = null
    private var myLocation by mutableStateOf(null as Location?)
    private val locationOverlay = SimpleLocationOverlay2(Marker.location)

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
        locationProvider.startLocationProvider(this)
    }

    fun disableMyLocation() {
        locationProvider.stopLocationProvider()
    }

    override fun onLocationChanged(location: Location?, source: IMyLocationProvider?) {
        updateMyLocation(location)
    }

    private fun updateMyLocation(newLocation: Location?) {
        val locationIsInitial = newLocation != null && myLocation == null
        //Log.d("INFO-", "updateMyLocation. $locationIsInitial: $locationIsInitial $newLocation")
        myLocation = newLocation
        locationOverlay.setLocation(myLocation?.let { GeoPoint(it) })
        if (locationIsInitial) {
            //mapInstance?.controller?.zoomTo(15.0)
            mapInstance?.controller?.animateTo(GeoPoint(myLocation!!), 15.0, null)
        } else {
            mapInstance?.postInvalidate()
        }
    }


    private fun initMapView(map: MapView) {
        Log.d("INFO-", "initMapView $map")
        mapInstance = map
        map.setMultiTouchControls(true)
        map.overlays.add(locationOverlay)

        //TODO: move to updateMapView
        if (myLocation == null) {
            map.controller.setZoom(10.0)
        } else {
            map.controller.animateTo(GeoPoint(myLocation!!), 15.0, null)
            //map.controller.zoomTo(12.0)
        }
    }

    private fun updateMapView(map: MapView) {
        Log.d("INFO-", "updateMapView $map $myLocation")
    }

    @Composable
    fun View() {
        Column(modifier = Modifier.fillMaxSize()) {

            Box(modifier = Modifier.weight(1f)) {
                MapView(
                    modifier = Modifier.fillMaxSize(),
                    onInitMapView = ::initMapView,
                    onUpdateMapView = ::updateMapView
                )
                Row(
                    modifier = Modifier.padding(start = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Button(onClick = { /*newMarker()*/ }) {
                        Text("О")
                    }
                    Button(onClick = { /*clearMarkers()*/ }) {
                        Text("Ф")
                    }
                }

            }
            Box() {
                val loc = myLocation
                Row {
                    Text("Лок-я: ")
                    if (loc == null) {
                        Text("определяется...")
                    } else {
                        Text(loc.info())
                    }
                }
            }
        }

    }
}

private fun Location.info() =
    "${DMS(latitude).prettyFormat()} ${DMS(longitude).prettyFormat()} ${accuracy.round(1)}"

private object Marker {
    val pylonDrawable: Drawable =
        circleBitmap(android.graphics.Color.RED).toDrawable(Resources.getSystem())
    val location = circleBitmap(android.graphics.Color.GREEN)
    val direction = circleBitmap(android.graphics.Color.BLACK)

    private fun circleBitmap(circleColor: Int): Bitmap {
        val bmp = Bitmap.createBitmap(31, 31, Bitmap.Config.ARGB_8888)
        val cnv = Canvas(bmp)
        cnv.drawCircle(
            15f,
            15f,
            10f,
            Paint().apply {
                style = Paint.Style.FILL_AND_STROKE
                strokeWidth = 6f
                this.color = circleColor
            })
        return bmp
    }
}