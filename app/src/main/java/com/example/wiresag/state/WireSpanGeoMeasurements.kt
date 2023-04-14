package com.example.wiresag.state

import com.example.wiresag.math.invoke

class WireSpanGeoMeasurements(val span: WireSpan) {

    val photoLine = PhotoLine(span)

    class PhotoLine(span: WireSpan) {
        val pointsWithDistances = photoPlacesSolver(span.pylon1.geoPoint, span.pylon2.geoPoint)
        val allPoints = pointsWithDistances.flatMap { listOf(it.point1, it.point2) }
        val normalPoints = pointsWithDistances.maxByOrNull { it.distance }!!
            .let { p -> p.point1 to p.point2 }
    }
}