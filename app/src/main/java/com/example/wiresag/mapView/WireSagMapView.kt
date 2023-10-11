package com.example.wiresag.mapView

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.wiresag.mapView.overlays.CanvasOverlay
import com.example.wiresag.mapView.overlays.MapViewMotionEvent
import com.example.wiresag.osmdroid.compose.MapView
import com.example.wiresag.osmdroid.enableRotationGesture
import com.example.wiresag.osmdroid.enableScaleBar
import com.example.wiresag.utils.addTo
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.views.MapView

class WireSagMapView(
    context: Context,
    onSingleTapConfirmed: MapViewMotionEvent? = null,
    onLongPress: MapViewMotionEvent?,
    onCanvasDraw: CanvasOverlay.DrawScope.() -> Unit = {}
) : MapView(context) {
    val canvas = CanvasOverlay(
        onDraw = onCanvasDraw,
        onSingleTapConfirmed = onSingleTapConfirmed,
        onLongPress = onLongPress
    ).addTo(overlays)

    init {
        setMultiTouchControls(true)
        enableScaleBar()
        enableRotationGesture()
    }
}

@Composable
fun WireSagMap(
    modifier: Modifier = Modifier,
    onInitMapView: (map: WireSagMapView) -> Unit = {},
    onUpdateMapView: (map: WireSagMapView) -> Unit = {},
    onSingleTapConfirmed: MapViewMotionEvent? = null,
    onLongPress: MapViewMotionEvent? = null,
    onZoom: ((zoomLevel: Double) -> Unit)? = null,
    onCanvasDraw: CanvasOverlay.DrawScope.() -> Unit = {}
) {
    MapView(
        modifier = modifier,
        factory = { context ->
            WireSagMapView(
                context = context,
                onSingleTapConfirmed = onSingleTapConfirmed,
                onLongPress = onLongPress,
                onCanvasDraw = onCanvasDraw
            ).addMapListener(onZoom)
        },
        onInitMapView = onInitMapView,
        onUpdateMapView = {
            it.canvas.evaluateDependencies()
            onUpdateMapView(it)
            it.postInvalidate()
        }
    )
}

private fun <MV : MapView> MV.addMapListener(
    onZoom: ((zoomLevel: Double) -> Unit)? = null
) = apply {
    if (onZoom != null) {
        addMapListener(
            object : MapListener {
                override fun onScroll(event: ScrollEvent?) = false
                override fun onZoom(event: ZoomEvent): Boolean {
                    onZoom(event.zoomLevel)
                    return true
                }

            }
        )
    }
}
