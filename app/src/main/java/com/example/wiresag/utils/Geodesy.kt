package com.example.wiresag.utils

import android.location.Location
import com.example.wiresag.utils.Location
import kotlin.math.*

const val EARTH_RADIUS = 6371000.0

fun Location.bigCircleDistanceTo(other: Location) =
    EARTH_RADIUS * sphericalAngle(other) //https://ru.wikipedia.org/wiki/Ортодромия

fun Location.sphericalAngle(other: Location) =
    sphericalAngleByDegrees(latitude, longitude, other.latitude, other.longitude)

inline fun sphericalAngleByDegrees(
    latInDegrees1: Double,
    lonInDegrees1: Double,
    latInDegrees2: Double,
    lonInDegrees2: Double
) = sphericalAngleByRadians(
    Math.toRadians(latInDegrees1),
    Math.toRadians(lonInDegrees1),
    Math.toRadians(latInDegrees2),
    Math.toRadians(lonInDegrees2)
)

inline fun sphericalAngleByRadians(
    latInRadian1: Double,
    lonInRadian1: Double,
    latInRadian2: Double,
    lonInRadian2: Double
) = acos(
    sin(latInRadian1) * sin(latInRadian2) + cos(latInRadian1) * cos(latInRadian2) * cos(lonInRadian2 - lonInRadian1)
)

fun Location.midpoint(other: Location): Location {
    return midpoint(
        Math.toRadians(latitude),
        Math.toRadians(longitude),
        Math.toRadians(other.latitude),
        Math.toRadians(other.longitude)
    ).let { (latRads, lonRads) ->
        Location(
            Math.toDegrees(latRads),
            Math.toDegrees(lonRads),
            provider
        )
    }
}

/// All angles are in radians!!!
fun midpoint(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Pair<Double, Double> {
    //https://www.movable-type.co.uk/scripts/latlong.html
    val deltaLambda = lon2 - lon1
    val bx = cos(lat2) * cos(deltaLambda)
    val by = cos(lat2) * sin(deltaLambda)
    val midLat = atan2(sin(lat1) + sin(lat2), sqrt(squared(cos(lat1) + bx) + squared(by)))
    val midLon = lon1 + atan2(by, cos(lat1) + bx)
    return midLat to midLon
    /*
    φ is lat
    λ is lon
    Bx = cos φ2 ⋅ cos Δλ
	By = cos φ2 ⋅ sin Δλ
	φm = atan2( sin φ1 + sin φ2, √(cos φ1 + Bx)² + By² )
	λm = λ1 + atan2(By, cos(φ1)+Bx)
    */
}

fun Location.soSimplifiedMidpoint(other: Location) =
    Location((latitude + other.latitude) / 2, (longitude + other.longitude) / 2)

private inline fun squared(a1: Double) = a1 * a1