package com.example.wiresag.location

import org.osmdroid.util.GeoPoint

interface GeoPointAware {
    val geoPoint: GeoPoint
}

data class GeoItemWithDistance<T>(val item: T, val distance: Double)

fun <T : GeoPointAware> List<T>.nearest(
    geoPoint: GeoPoint,
    maxDistance: Double = Double.POSITIVE_INFINITY
) = map { GeoItemWithDistance(it, it.geoPoint.distanceToAsDouble(geoPoint)) }
    .minByOrNull { it.distance }
    ?.takeIf { it.distance <= maxDistance }

fun <T : GeoPointAware> List<T>.nearest(
    geoItem: GeoPointAware,
    maxDistance: Double = Double.POSITIVE_INFINITY
) = nearest(geoItem.geoPoint, maxDistance)