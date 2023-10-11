package com.example.wiresag.state

import androidx.compose.runtime.mutableStateListOf
import com.example.wiresag.location.GeoPointAware
import com.example.wiresag.location.midpoint
import org.osmdroid.util.GeoPoint

data class WireSpan(
    val pylon1: Pylon,
    val pylon2: Pylon
) : GeoPointAware {
    override val geoPoint: GeoPoint get() = midpoint
    val photos = mutableStateListOf<WireSpanPhoto>()
    val length by lazy {
        pylon1.geoPoint.distanceToAsDouble(pylon2.geoPoint).toFloat()
    }
    private val midpoint by lazy {
        pylon1.geoPoint.midpoint(pylon2.geoPoint)
    }
}