package com.example.wiresag.viewModel

import android.graphics.Color
import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.wiresag.R
import com.example.wiresag.location.info
import com.example.wiresag.mapView.WireSagMap
import com.example.wiresag.mapView.overlays.CanvasOverlay
import com.example.wiresag.state.Pylon
import com.example.wiresag.state.WireSpan
import com.example.wiresag.state.WireSpanGeoMeasurements
import com.example.wiresag.ui.components.Icon
import com.example.wiresag.ui.components.TransparentButton
import com.example.wiresag.ui.drawCircle
import com.example.wiresag.ui.drawLine
import com.example.wiresag.ui.image.annotation.WireSagAnnotationTool
import org.osmdroid.util.GeoPoint

@Composable
fun WireSagViewModel.View() {
    SaveDependencyEval()
    WireSagAnnotation()
    Map()
}

@Composable
fun WireSagViewModel.WireSagAnnotation() {
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
private fun WireSagViewModel.MapControls() {

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
private fun WireSagViewModel.Map() {
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
            onZoom = { zoomLevel = it }
        ) {
            with(ObjectsDrawer) {
                draw(
                    zoomLevel,
                    currentLocation,
                    objectContext.pylons,
                    objectContext.spans,
                    nearestSpanMeasurements
                )
            }
        }

    }
}

@Composable
private fun WireSagViewModel.SaveDependencyEval() {
    objectContext.saveRequest
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


private object ObjectsDrawer {
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
        val span = NativePaint().apply {
            style = android.graphics.Paint.Style.FILL_AND_STROKE
            strokeWidth = 1f
            color = Color.argb(127, 0, 0, 255)
        }
        val nearestSpan = NativePaint().apply {
            style = android.graphics.Paint.Style.FILL_AND_STROKE
            strokeWidth = 2.5f
            color = Color.argb(255, 0, 0, 255)
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
            style = android.graphics.Paint.Style.FILL_AND_STROKE
            strokeWidth = 1f
            color = Color.BLACK
        }
    }

    fun CanvasOverlay.DrawScope.draw(
        zoomLevel: Double,
        currentLocation: GeoPoint?,
        pylons: List<Pylon>,
        wireSpans: List<WireSpan>,
        nearestSpanMeasurements: WireSpanGeoMeasurements?
    ) {
        pylons.forEach { pylon ->
            canvas.drawCircle(pylon.geoPoint.toPixelF(), 10f, Paints.pylon)
        }

        wireSpans.forEach { span ->
            canvas.drawLine(
                span.pylon1.geoPoint.toPixelF(),
                span.pylon2.geoPoint.toPixelF(),
                Paints.span
            )
            span.photos.forEach {
                canvas.drawCircle(it.photoWithGeoPoint.geoPoint.toPixelF(), 10f, Paints.photo)
            }
        }

        nearestSpanMeasurements?.run {
            canvas.drawLine(
                span.pylon1.geoPoint.toPixelF(),
                span.pylon2.geoPoint.toPixelF(),
                Paints.nearestSpan
            )

            photoLine.normalPoints.let { (gp1, gp2) ->
                canvas.drawLine(gp1.toPixelF(), gp2.toPixelF(), Paints.normal)
            }
            photoLine.allPoints.forEach {
                canvas.drawCircle(it.toPixelF(), 5f, Paints.placeForPhoto)
            }
        }

        currentLocation?.run {
            canvas.drawCircle(toPixelF(), 10f, Paints.location)
        }
    }
}
