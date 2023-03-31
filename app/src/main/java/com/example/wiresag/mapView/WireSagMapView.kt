package com.example.wiresag.mapView

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.wiresag.osmdroid.compose.MapView
import com.example.wiresag.osmdroid.enableRotationGesture
import com.example.wiresag.osmdroid.enableScaleBar
import com.example.wiresag.utils.addTo
import org.osmdroid.views.MapView

class WireSagMapView(
    context: Context,
    onSingleTapConfirmed: OverlayMotionEvent? = null,
    onLongPress: OverlayMotionEvent?,
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
    onSingleTapConfirmed: OverlayMotionEvent? = null,
    onLongPress: OverlayMotionEvent?,
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
            )
        },
        onInitMapView = onInitMapView,
        onUpdateMapView = {
            it.canvas.evaluateDrawDependencies()
            onUpdateMapView(it)
            it.postInvalidate()
        }
    )
}