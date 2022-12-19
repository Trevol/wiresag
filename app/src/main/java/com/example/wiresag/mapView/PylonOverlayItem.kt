package com.example.wiresag.mapView

import com.example.wiresag.state.Pylon

class PylonOverlayItem(val pylon: Pylon) :
    CenteredOverlayItem(pylon.name, pylon.geoPoint)

