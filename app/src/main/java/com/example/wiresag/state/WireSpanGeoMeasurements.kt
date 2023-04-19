package com.example.wiresag.state

import com.example.wiresag.math.invoke
import org.osmdroid.util.GeoPoint

class WireSpanGeoMeasurements(val span: WireSpan, location: GeoPoint) {
    val photoLine = PhotoLine(span)

    val distancesToPoints = listOf(
        distanceBetweenGeoPoints(span.geoPoint, location),
        distanceBetweenGeoPoints(span.pylon1.geoPoint, location),
        distanceBetweenGeoPoints(span.pylon2.geoPoint, location)
    )

    class PhotoLine(span: WireSpan) {
        val pointsWithDistances = photoPlacesSolver(span.pylon1.geoPoint, span.pylon2.geoPoint)
        val allPoints = pointsWithDistances.flatMap { listOf(it.point1, it.point2) }
        val normalPoints = pointsWithDistances.maxByOrNull { it.distance }!!
            .let { p -> p.point1 to p.point2 }
    }

}

data class DistanceBetweenGeoPoints(
    val point1: GeoPoint,
    val point2: GeoPoint,
    val distance: Double
)

fun distanceBetweenGeoPoints(point1: GeoPoint, point2: GeoPoint) =
    DistanceBetweenGeoPoints(point1, point2, point1.distanceToAsDouble(point2))