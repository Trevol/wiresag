package com.example.wiresag.osmdroid.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.wiresag.osmdroid.compose.rememberMapViewWithLifecycle
import org.osmdroid.views.MapView

//https://gist.github.com/ArnyminerZ/418683e3ef43ccf1268f9f62940441b1
//https://gist.github.com/ArnyminerZ/5ef0c46c4c3d42045005dd3459735aec
/**
 * A composable Google Map.
 * @author Arnau Mora
 * @since 20211230
 * @param modifier Modifiers to apply to the map.
 * @param onLoad This will get called once the map has been loaded.
 */
@Composable
fun MapView(
    modifier: Modifier = Modifier,
    onLoad: ((map: MapView) -> Unit)? = null
) {
    val mapViewState = rememberMapViewWithLifecycle()

    AndroidView(
        { mapViewState },
        modifier
    ) { mapView -> onLoad?.invoke(mapView) }
}