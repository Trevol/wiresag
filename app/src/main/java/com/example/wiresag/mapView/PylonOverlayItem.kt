package com.example.wiresag.mapView

import com.example.wiresag.model.Pylon
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.OverlayItem

class PylonOverlayItem(val pylon: Pylon) :
    OverlayItem(pylon.name, "", GeoPoint(pylon.latitude, pylon.longitude))