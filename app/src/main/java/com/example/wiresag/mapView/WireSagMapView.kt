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

class WireSagMapView(context: Context, onCanvasDraw: CanvasOverlay.DrawScope.() -> Unit = {}) :
    MapView(context) {

    val canvas = CanvasOverlay(onCanvasDraw).addTo(overlays)
    //val overlay = Overlays(this, onCanvasDraw)

    init {
        setMultiTouchControls(true)
        enableScaleBar()
        enableRotationGesture()
    }

    /*class Overlays(map: WireSagMapView, onCanvasDraw: CanvasOverlay.DrawScope.() -> Unit = {}) {
        val location = SimpleLocationOverlay2(MapViewMarker.location).addTo(map.overlays)

        val pylons = ItemizedIconOverlay2(
            mutableListOf<PylonOverlayItem>(),
            MapViewMarker.pylon.toDrawable(Resources.getSystem()),
            null,
            map.context
        ).addTo(map.overlays)

        val placesForPhoto = ItemizedIconOverlay2(
            mutableListOf<CenteredOverlayItem>(),
            MapViewMarker.photoPlace.toDrawable(Resources.getSystem()),
            null,
            map.context
        ).addTo(map.overlays)

         val canvas = CanvasOverlay(onCanvasDraw).addTo(map.overlays)
    }*/
}

@Composable
fun WireSagMap(
    modifier: Modifier = Modifier,
    onInitMapView: (map: WireSagMapView) -> Unit = {},
    onUpdateMapView: (map: WireSagMapView) -> Unit = {},
    onCanvasDraw: CanvasOverlay.DrawScope.() -> Unit = {}
) {
    MapView(
        modifier,
        factory = { context -> WireSagMapView(context, onCanvasDraw) },
        onInitMapView,
        {
            it.canvas.evaluateDrawDependencies()
            onUpdateMapView(it)
        }
    )
}