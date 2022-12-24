package com.example.wiresag.viewModel

import android.content.Context
import android.graphics.Color
import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.preference.PreferenceManager
import com.example.wiresag.R
import com.example.wiresag.camera.PhotoRequest
import com.example.wiresag.mapView.*
import com.example.wiresag.osmdroid.toGeoPoint
import com.example.wiresag.state.GeoObjects
import com.example.wiresag.state.PhotoWithGeoPoint
import com.example.wiresag.state.WireSpanPhoto
import com.example.wiresag.ui.components.Icon
import com.example.wiresag.ui.components.TransparentButton
import com.example.wiresag.ui.image.annotation.WireSagAnnotationTool
import com.example.wiresag.utils.*
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider

class WireSagViewModel(
    applicationContext: Context,
    private val locationProvider: IMyLocationProvider,
    private val photoRequest: PhotoRequest
) {
    private var currentLocation by mutableStateOf(null as Location?)
    private val currentGeoPoint by derivedStateOf { currentLocation?.toGeoPoint() }
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
        val locationIsInitial = currentLocation != null && prevLocation == null
        if (locationIsInitial) {
            map.controller.animateTo(GeoPoint(currentLocation!!), 19.5, null)
            prevLocation = currentLocation // animate only once!!!
        }
        map.postInvalidate()
    }

    private object Paints {
        val location = NativePaint().apply {
            style = android.graphics.Paint.Style.FILL
            strokeWidth = 0f
            color = Color.GREEN
        }
        val pylon = NativePaint().apply {
            style = android.graphics.Paint.Style.FILL
            strokeWidth = 0f
            color = Color.argb(127, 0, 0, 255)
        }
        val placeForPhoto = NativePaint().apply {
            style = android.graphics.Paint.Style.FILL
            strokeWidth = 0f
            color = Color.argb(127, 0, 0, 0)
        }
        val normal = NativePaint().apply {
            style = android.graphics.Paint.Style.FILL
            strokeWidth = 0f
            color = Color.BLACK
        }
    }

    private fun CanvasOverlay.DrawScope.mapCanvasDraw() {
        geoObjects.pylons.forEachIndexed { i, pylon ->
            val px = pylon.geoPoint.toPixelF()
            if (i > 0) {
                val prevPx = geoObjects.pylons[i - 1].geoPoint.toPixelF()
                canvas.drawLine(px.x, px.y, prevPx.x, prevPx.y, Paints.pylon)
            }
            canvas.drawCircle(px.x, px.y, 10f, Paints.pylon)
        }

        geoObjects.spans.forEach { span ->
            span.photoLine.normalPoints
                .map { it.toPixelF() }
                .let { (px1, px2) ->
                    canvas.drawLine(px1.x, px1.y, px2.x, px2.y, Paints.normal)
                }
            span.photoLine.allPoints
                .map { it.toPixelF() }
                .forEach { px ->
                    canvas.drawCircle(px.x, px.y, 5f, Paints.placeForPhoto)
                }
        }

        currentGeoPoint?.run {
            val px = toPixelF()
            canvas.drawCircle(px.x, px.y, 10f, Paints.location)
        }
    }

    private fun markPylon() {
        currentLocation?.run {
            geoObjects.markPylon(toGeoPoint())
        }
    }

    private val maxDistanceFromPhotoToSpan = 200.0

    private fun takePhotoWithLocation() {
        currentLocation ?: return
        photoRequest.takePhoto { photo ->
            val photo = photo ?: return@takePhoto
            val currentGeoPoint = currentLocation?.toGeoPoint() ?: return@takePhoto
            val span = geoObjects.nearestWireSpan(currentGeoPoint, maxDistanceFromPhotoToSpan)
                ?: return@takePhoto
            photoForAnnotation =
                span.photos.addItem(WireSpanPhoto(span, PhotoWithGeoPoint(photo, currentGeoPoint)))
        }
    }

    private fun navigateToCurrentLocation(location: Location?) {
        if (location != null) {
            //TODO: Navigate!!!
            println("TODO: Navigate!!!")
        }
    }

    @Composable
    fun View() {

        if (photoForAnnotation != null) {
            WireSagAnnotationTool(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(2f),
                spanPhoto = photoForAnnotation!!,
                onClose = { photoForAnnotation = null },
                onDelete = {
                    photoForAnnotation?.span?.photos?.remove(photoForAnnotation)
                    photoForAnnotation = null
                }
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {

                Row(modifier = Modifier.zIndex(1f)) {
                    Spacer(Modifier.weight(1f))

                    TransparentButton(
                        onClick = { markPylon() },
                        enabled = currentLocation != null
                    ) {
                        Icon(R.drawable.ic_outline_add_location_alt_24)
                    }

                    TransparentButton(
                        onClick = { takePhotoWithLocation() },
                        enabled = currentLocation != null && geoObjects.pylons.size > 1
                    ) {
                        Icon(R.drawable.ic_baseline_add_a_photo_24)
                    }
                }

                WireSagMap(
                    modifier = Modifier.fillMaxSize(),
                    onInitMapView = {
                        it.controller.setZoom(15.0)
                    },
                    onUpdateMapView = ::updateMapView,
                    onCanvasDraw = { mapCanvasDraw() }
                )
            }
            LocationInfo(currentLocation, onClick = ::navigateToCurrentLocation)
        }

    }
}

@Composable
private fun LocationInfo(location: Location?, onClick: (Location?) -> Unit = {}) {
    Row(Modifier.clickable { onClick(location) }, verticalAlignment = Alignment.CenterVertically) {
        if (location == null) {
            Icon(R.drawable.ic_outline_location_searching_12)
        } else {
            Icon(R.drawable.ic_outline_my_location_12)
            Text(location.info(), fontSize = 10.sp)
        }
    }
}

private fun Location.info(): String {
    val speedInfo = if (hasSpeed()) speed.round(3) else ""
    val latLon = "${DMS(latitude).prettyFormat()}, ${DMS(longitude).prettyFormat()}"
    return "$provider: $latLon   ${accuracy.round(1)}   $speedInfo"
}
