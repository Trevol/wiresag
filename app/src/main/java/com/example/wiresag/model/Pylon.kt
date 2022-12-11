package com.example.wiresag.model

import com.example.wiresag.utils.DMS
import org.osmdroid.util.GeoPoint

class Pylon(val geoPoint: GeoPoint) {
    val name = "Опора ${DMS(geoPoint.latitude)}/${DMS(geoPoint.longitude)}"
}