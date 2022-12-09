package com.example.wiresag.osmdroid

import android.graphics.Bitmap
import android.location.Location
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MinimapOverlay
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.SimpleLocationOverlay

fun MapView.enableMyLocation(icon: Bitmap? = null): MapView {
    SimpleLocationOverlay(icon)
        .also { overlays.add(it) }
    return this
}

/*fun MapView.enableMyLocation(personIcon: Bitmap? = null, directionIcon: Bitmap? = null): MapView {
    MyLocationNewOverlay(GpsMyLocationProvider(context), this)
        .apply {
            personIcon?.also { setPersonIcon(it) }
            directionIcon?.also { setDirectionIcon(it) }
            enableMyLocation()
            // enableFollowLocation()
        }
        .also { overlays.add(it) }
    return this
}*/

fun MapView.enableCompass(): MapView {
    CompassOverlay(context, InternalCompassOrientationProvider(context), this)
        .apply { enableCompass() }
        .also { overlays.add(it) }
    return this
}


fun MapView.enableScaleBar(): MapView {
    ScaleBarOverlay(this).apply {
        setCentred(true)
        setScaleBarOffset(context.resources.displayMetrics.widthPixels / 2, 10)
    }.also { overlays.add(it) }
    return this
}

fun MapView.enableMinimap(): MapView {
    MinimapOverlay(context, tileRequestCompleteHandler)
        .apply {
            width = context.resources.displayMetrics.widthPixels / 5
            height = context.resources.displayMetrics.heightPixels / 5
            //optionally, you can set the minimap to a different tile source
            //setTileSource(....)
        }.also { overlays.add(it) }
    return this
}

fun MapView.enableRotationGesture(): MapView {
    RotationGestureOverlay(this).apply {
        this.isEnabled = true
    }.also { overlays.add(it) }
    return this
}

inline fun Location.toGeoPoint() = GeoPoint(this)