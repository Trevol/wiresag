package com.example.wiresag.mapView

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.graphics.drawable.toDrawable
import com.example.wiresag.osmdroid.SimpleLocationOverlay2
import com.example.wiresag.osmdroid.compose.MapView
import com.example.wiresag.osmdroid.enableRotationGesture
import com.example.wiresag.osmdroid.enableScaleBar
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay

class WireSagMapView(context: Context) : MapView(context) {
    val locationOverlay = SimpleLocationOverlay2(MapViewMarker.location)
        .also { overlays.add(it) }

    val pylonsOverlay = ItemizedIconOverlay2(
        mutableListOf<PylonOverlayItem>(),
        MapViewMarker.pylon.toDrawable(Resources.getSystem()),
        null,
        context
    ).also { overlays.add(it) }

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
    onUpdateMapView: (map: WireSagMapView) -> Unit = {}
) {
    MapView(
        modifier,
        factory = { context -> WireSagMapView(context) },
        onInitMapView,
        onUpdateMapView
    )
}