package com.example.wiresag.mapView

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.wiresag.osmdroid.SimpleLocationOverlay2
import com.example.wiresag.osmdroid.compose.MapView
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay

class WireSagMapView(context: Context) : MapView(context) {
    val locationOverlay = SimpleLocationOverlay2(MapViewMarker.location)
        .also { overlays.add(it) }

    val pylonsOverlay = ItemizedIconOverlay2(
        mutableListOf<PylonOverlayItem>(),
        MapViewMarker.pylonDrawable,
        ItemGestureListener,
        context
    ).also { overlays.add(it) }

    init {
        setMultiTouchControls(true)
    }

    object ItemGestureListener : ItemizedIconOverlay.OnItemGestureListener<PylonOverlayItem> {
        override fun onItemSingleTapUp(index: Int, item: PylonOverlayItem?): Boolean {
            return true
        }

        override fun onItemLongPress(index: Int, item: PylonOverlayItem?): Boolean {
            return false
        }
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