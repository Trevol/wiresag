package com.example.wiresag.mapView

import com.example.wiresag.model.Pylon
import com.example.wiresag.osmdroid.toGeoPoint

class PylonOverlayItem(val pylon: Pylon) :
    CenteredOverlayItem(pylon.name, pylon.geoPoint)

