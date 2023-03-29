package com.example.wiresag.mapView

import android.content.Context
import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.graphics.drawable.toDrawable
import com.example.wiresag.osmdroid.SimpleLocationOverlay2
import com.example.wiresag.osmdroid.compose.MapView
import com.example.wiresag.osmdroid.enableRotationGesture
import com.example.wiresag.osmdroid.enableScaleBar
import com.example.wiresag.utils.addTo
import org.osmdroid.views.MapView

class WireSagMapView(
    context: Context,
    onSingleTapConfirmed: OverlayMotionEvent? = null,
    onCanvasDraw: CanvasOverlay.DrawScope.() -> Unit = {}
) : MapView(context) {
    val canvas = CanvasOverlay(onDraw = onCanvasDraw, onSingleTapConfirmed = onSingleTapConfirmed)
        .addTo(overlays)

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
    onCanvasDraw: CanvasOverlay.DrawScope.() -> Unit = {}
) {
    MapView(
        modifier,
        factory = { context ->
            WireSagMapView(
                context = context,
                onSingleTapConfirmed = onSingleTapConfirmed,
                onCanvasDraw = onCanvasDraw
            )
        },
        onInitMapView,
        {
            it.canvas.evaluateDrawDependencies()
            onUpdateMapView(it)
            it.postInvalidate()
        }
    )
}