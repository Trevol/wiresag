package com.example.wiresag.math.geo

import android.location.Location
import com.example.wiresag.location.LocationRadians
import com.example.wiresag.location.toLocation
import com.example.wiresag.location.toLocationRadians
import com.example.wiresag.math.squared
import kotlin.math.*

open class Sphere(val r: Double) {
    fun distance(centralAngle: Double) = centralAngle * r

    fun distance(p1: Location, p2: Location) =
        //https://ru.wikipedia.org/wiki/Ортодромия
        distance(p1.centralAngle(p2))

    fun distance(p1: LocationRadians, p2: LocationRadians) =
        //https://ru.wikipedia.org/wiki/Ортодромия
        distance(p1.centralAngle(p2))

    fun centralAngle(arcLength: Double) = arcLength / r
}

fun Location.distance(other: Location, sphere: Sphere) = sphere.distance(this, other)
fun LocationRadians.distance(other: LocationRadians, sphere: Sphere) = sphere.distance(this, other)

fun Location.centralAngle(other: Location) =
    toLocationRadians().centralAngle(other.toLocationRadians())

fun Location.centralAngleCos(other: Location) =
    toLocationRadians().centralAngleCos(other.toLocationRadians())

fun LocationRadians.centralAngle(other: LocationRadians) =
    acos(centralAngleCos(other))

fun LocationRadians.centralAngleCos(other: LocationRadians) =
    sin(latitude) * sin(other.latitude) + cos(latitude) * cos(other.latitude) * cos(other.longitude - longitude)

fun Location.midpoint(other: Location) = toLocationRadians().midpoint(other.toLocationRadians()).toLocation()


fun LocationRadians.midpoint(other: LocationRadians): LocationRadians {
    //https://www.movable-type.co.uk/scripts/latlong.html
    val deltaLambda = other.longitude - longitude
    val bx = cos(other.latitude) * cos(deltaLambda)
    val by = cos(other.latitude) * sin(deltaLambda)
    val midLat = atan2(sin(latitude) + sin(other.latitude), sqrt(squared(cos(latitude) + bx) + squared(by)))
    val midLon = longitude + atan2(by, cos(latitude) + bx)
    return LocationRadians(midLat, midLon)
    /*
    φ is lat
    λ is lon
    Bx = cos φ2 ⋅ cos Δλ
	By = cos φ2 ⋅ sin Δλ
	φm = atan2( sin φ1 + sin φ2, √(cos φ1 + Bx)² + By² )
	λm = λ1 + atan2(By, cos(φ1)+Bx)
    */
}

// Является прямым сферический угол с центром в centre
// Успользуется теорема косинусов для прямоугольного сферического трейгольника
fun isRightAngle(p1: LocationRadians, center: LocationRadians, p2: LocationRadians, epsilon: Double = 1e-12): Boolean {
    // cos(p1_p2) = cos(p1_center)*cos(p2_center)
    return abs(p1.centralAngleCos(p2) - p1.centralAngleCos(center) * p2.centralAngleCos(center)) <= epsilon
}




