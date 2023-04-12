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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.preference.PreferenceManager
import com.example.wiresag.R
import com.example.wiresag.camera.PhotoRequest
import com.example.wiresag.location.Location
import com.example.wiresag.location.info
import com.example.wiresag.mapView.WireSagMap
import com.example.wiresag.mapView.WireSagMapView
import com.example.wiresag.mapView.overlays.CanvasOverlay
import com.example.wiresag.mapView.overlays.MapViewMotionEvent
import com.example.wiresag.mapView.overlays.MapViewMotionEventScope
import com.example.wiresag.osmdroid.StubLocationProvider
import com.example.wiresag.osmdroid.toGeoPoint
import com.example.wiresag.state.ObjectContext
import com.example.wiresag.state.WireSpanPhoto
import com.example.wiresag.ui.components.Icon
import com.example.wiresag.ui.components.TransparentButton
import com.example.wiresag.ui.drawCircle
import com.example.wiresag.ui.drawLine
import com.example.wiresag.ui.image.annotation.WireSagAnnotationTool
import org.osmdroid.config.Configuration
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider

class WireSagViewModel(
    applicationContext: Context,
    private val locationProvider: IMyLocationProvider,
    private val photoRequest: PhotoRequest,
    private val objectContext: ObjectContext,
) {
    private var zoomLevel by mutableStateOf(15.0)

    private var providerLocation by mutableStateOf(null as Location?)
    private var isInitialLocation = false
    private var initialLocationAnimated = false
    private val currentLocation by derivedStateOf { providerLocation?.toGeoPoint() }

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
        if (providerLocation == null && newLocation != null) {
            isInitialLocation = true
        }
        providerLocation = newLocation
    }

    private fun updateMapView(map: WireSagMapView) {
        if (isInitialLocation && !initialLocationAnimated) {
            map.controller.animateTo(currentLocation!!, 19.5, null)
            initialLocationAnimated = true
        }
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
        val photo = NativePaint().apply {
            style = android.graphics.Paint.Style.FILL
            strokeWidth = 0f
            color = Color.argb(255, 200, 0, 0)
        }
        val normal = NativePaint().apply {
            style = android.graphics.Paint.Style.FILL
            strokeWidth = 0f
            color = Color.BLACK
        }
    }

    private fun CanvasOverlay.DrawScope.mapCanvasDraw() {
        objectContext.pylons.forEach { pylon ->
            canvas.drawCircle(pylon.geoPoint.toPixelF(), 10f, Paints.pylon)
        }

        objectContext.spans.forEach { span ->
            canvas.drawLine(
                span.pylon1.geoPoint.toPixelF(),
                span.pylon2.geoPoint.toPixelF(),
                Paints.pylon
            )
            span.photoLine.normalPoints.let { (gp1, gp2) ->
                canvas.drawLine(gp1.toPixelF(), gp2.toPixelF(), Paints.normal)
            }
            span.photoLine.allPoints.forEach {
                canvas.drawCircle(it.toPixelF(), 5f, Paints.placeForPhoto)
            }
            span.photos.forEach {
                canvas.drawCircle(it.photoWithGeoPoint.geoPoint.toPixelF(), 10f, Paints.photo)
            }
        }

        currentLocation?.run {
            canvas.drawCircle(toPixelF(), 10f, Paints.location)
        }
    }

    private fun markPylon() {
        currentLocation?.run {
            objectContext.markPylon(this)
        }
    }

    private val maxDistanceFromPhotoToSpan = 200.0

    private fun takePhotoWithLocation() {
        currentLocation ?: return
        photoRequest.takePhoto { photo ->
            val photo = photo ?: return@takePhoto
            val photoLocation = currentLocation ?: return@takePhoto
            val span = objectContext.nearestWireSpan(photoLocation, maxDistanceFromPhotoToSpan)
                ?: return@takePhoto
            photoForAnnotation = objectContext.addWireSpanPhoto(span, photo, photoLocation)
        }
    }

    private fun onSingleTapConfirmed(d: MapViewMotionEventScope): Boolean {
        val tappedPhoto = objectContext.nearestSpanPhoto(d.geoPoint, maxDistance = 3.0)
        photoForAnnotation = tappedPhoto
        return tappedPhoto != null
    }

    private fun navigateToLocation(location: Location?) {
        if (location != null) {
            //TODO: Navigate!!! But how?
        }
    }

    private fun initLongPressHandler(): MapViewMotionEvent? =
        if (locationProvider is StubLocationProvider) {
            {
                providerLocation = it.geoPoint.run { Location(latitude, longitude) }
                true
            }
        } else null

    // VIEWS ************************
    @Composable
    fun WireSagAnnotation() {
        if (photoForAnnotation != null) {
            WireSagAnnotationTool(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                spanPhoto = photoForAnnotation!!,
                imageById = { id -> objectContext.readImage(id) },
                onClose = {
                    photoForAnnotation = null
                },
                onDelete = {
                    if (photoForAnnotation != null) {
                        objectContext.deleteSpanPhoto(photoForAnnotation!!)
                        photoForAnnotation = null
                    }
                }
            )
        }
    }

    @Composable
    fun MapControls() {

        @Composable
        fun UpperButtons() {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

                TransparentButton(
                    onClick = { markPylon() },
                    enabled = currentLocation != null
                ) {
                    Icon(R.drawable.ic_outline_add_location_alt_24)
                }

                TransparentButton(
                    onClick = { takePhotoWithLocation() },
                    enabled = currentLocation != null && objectContext.pylons.size > 1
                ) {
                    Icon(R.drawable.ic_baseline_add_a_photo_24)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            UpperButtons()

            LocationInfo(
                providerLocation,
                onClick = ::navigateToLocation
            )
        }

    }

    @Composable
    fun Map() {
        Box(modifier = Modifier.fillMaxSize()) {
            MapControls()
            WireSagMap(
                modifier = Modifier.fillMaxSize(),
                onInitMapView = {
                    it.controller.setZoom(zoomLevel)
                },
                onUpdateMapView = ::updateMapView,
                onSingleTapConfirmed = ::onSingleTapConfirmed,
                onLongPress = initLongPressHandler(),
                onZoom = { zoomLevel = it },
                onCanvasDraw = { mapCanvasDraw() }
            )

        }
    }

    @Composable
    fun View() {
        SaveDependencyEval()
        WireSagAnnotation()
        Map()
    }

    @Composable
    fun SaveDependencyEval() {
        objectContext.saveRequest
    }
}

@Composable
private fun LocationInfo(
    location: Location?,
    modifier: Modifier = Modifier,
    onClick: (Location?) -> Unit = {}
) {
    Row(
        modifier
            .padding(start = 4.dp)
            .clickable { onClick(location) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (location == null) {
            Icon(R.drawable.ic_outline_location_searching_24)
            Text("Searching...")
        } else {
            Icon(R.drawable.ic_outline_my_location_12)
            Text(location.info(), fontSize = 10.sp)
        }
    }
}
