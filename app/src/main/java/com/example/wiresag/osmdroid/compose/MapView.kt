package com.example.wiresag.osmdroid.compose

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.views.MapView

//https://gist.github.com/ArnyminerZ/418683e3ef43ccf1268f9f62940441b1
//https://gist.github.com/ArnyminerZ/5ef0c46c4c3d42045005dd3459735aec


@Composable
fun <MV : MapView> MapView(
    modifier: Modifier = Modifier,
    factory: (Context) -> MV,
    onInitMapView: (map: MV) -> Unit = {},
    onUpdateMapView: (map: MV) -> Unit = {}
) {
    val mapViewState = rememberMapViewWithLifecycle(factory)
    AndroidView(
        factory = {
            onInitMapView(mapViewState)
            mapViewState
        },
        modifier = modifier,
        update = onUpdateMapView
    )
}

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    onInitMapView: (map: MapView) -> Unit = {},
    onUpdateMapView: (map: MapView) -> Unit = {}
) {
    MapView(
        modifier,
        factory = { context -> MapView(context) },
        onInitMapView,
        onUpdateMapView
    )
}