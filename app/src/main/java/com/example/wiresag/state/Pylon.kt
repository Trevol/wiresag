package com.example.wiresag.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import com.example.wiresag.location.GeoPointAware
import com.example.wiresag.math.squareDistance
import com.example.wiresag.utils.DMS
import org.osmdroid.util.GeoPoint


data class Pylon(override val geoPoint: GeoPoint) : GeoPointAware {
    val name = "Опора ${DMS(geoPoint.latitude)}/${DMS(geoPoint.longitude)}"
    val spans: MutableList<WireSpan> = mutableStateListOf()
}