package com.example.wiresag.state

import androidx.compose.runtime.mutableStateListOf
import com.example.wiresag.location.GeoPointAware
import com.example.wiresag.location.midpoint
import com.example.wiresag.math.invoke
import org.osmdroid.util.GeoPoint

data class WireSpan(
    val pylon1: Pylon,
    val pylon2: Pylon
) : GeoPointAware {
    override val geoPoint: GeoPoint get() = midpoint

    val length by lazy { pylon1.geoPoint.distanceToAsDouble(pylon2.geoPoint).toFloat() }
    val midpoint by lazy { pylon1.geoPoint.midpoint(pylon2.geoPoint) }
    val photos = mutableStateListOf<WireSpanPhoto>()
    val photoLine by lazy { PhotoLine(this) }

    class PhotoLine(span: WireSpan) {
        val pointsWithDistances = photoPlacesSolver(span.pylon1.geoPoint, span.pylon2.geoPoint)
        val allPoints = pointsWithDistances.flatMap { listOf(it.point1, it.point2) }
        val normalPoints = pointsWithDistances.maxByOrNull { it.distance }!!
            .let { p -> p.point1 to p.point2 }
    }
}