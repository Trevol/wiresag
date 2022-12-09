package com.example.wiresag.mapView

import org.osmdroid.api.IGeoPoint
import org.osmdroid.views.overlay.OverlayItem

open class CenteredOverlayItem(title: String = "", geoPoint: IGeoPoint) :
    OverlayItem(title, "", geoPoint) {
    init {
        markerHotspot = HotspotPlace.CENTER
    }
}