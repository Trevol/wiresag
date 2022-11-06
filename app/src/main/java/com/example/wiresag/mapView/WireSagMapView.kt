package com.example.wiresag.mapView

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.wiresag.MapViewMarker
import com.example.wiresag.osmdroid.SimpleLocationOverlay2
import com.example.wiresag.osmdroid.compose.MapView
import org.osmdroid.views.MapView

class WireSagMapView(context: Context) : MapView(context) {
    val locationOverlay = SimpleLocationOverlay2(MapViewMarker.location)
        .also { overlays.add(it) }

    //TODO: init overlay for objects (pylons, photo places etc)
    init {
        setMultiTouchControls(true)
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