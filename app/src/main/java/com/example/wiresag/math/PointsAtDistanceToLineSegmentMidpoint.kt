package com.example.wiresag.math

import android.location.Location
import com.example.wiresag.location.LocationRadians
import com.example.wiresag.location.plus
import com.example.wiresag.location.toLocation
import com.example.wiresag.location.toLocationRadians
import math.*
import com.example.wiresag.math.geo.centralAngle
import com.example.wiresag.math.geo.midpoint
import com.example.wiresag.utils.map
import org.osmdroid.api.IGeoPoint
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

class PointsAtDistanceToLineSegmentMidpoint(val angularDistanceToMidpoint: Double) {

    operator fun invoke(segment: Pair<LocationRadians, LocationRadians>) =
        extendedInvoke(segment).locations

    data class ExtendedResult(
        val locations: Pair<LocationRadians, LocationRadians>,
        val segmentIn2D: Pair<Point, Point>,
        val points: Pair<Point, Point>
    )

    fun extendedInvoke(segment: Pair<LocationRadians, LocationRadians>): ExtendedResult {
        if (segment.first == segment.second) {
            throw Exception("Unexpected: segment.first == segment.second")
        }
        val (origin, segmentIn2D) = to2D(segment)
        val resultPoints = pointsIn2D(segmentIn2D, angularDistanceToMidpoint)
        val resultLocations = toSpherical(resultPoints, origin)
        return ExtendedResult(resultLocations, segmentIn2D, resultPoints)
    }

    private fun pointsIn2D(segment: Pair<Point, Point>, distance: Double): Pair<Point, Point> {
        val m = segment.first.midpoint(segment.second)
        val abNormalAngle = atan(LineEquation(segment).normalLine(m).k)
        val dx = distance * cos(abNormalAngle)
        val dy = distance * sin(abNormalAngle)
        return Point(m.x + dx, m.y + dy) to Point(m.x - dx, m.y - dy)
    }

    private data class SphericalToXYMapping(
        val origin: LocationRadians,
        val segment: Pair<Point, Point>
    )

    private fun to2D(segment: Pair<LocationRadians, LocationRadians>): SphericalToXYMapping {
        // ось x - долгота, ось y - широта
        val deltaLat = segment.second.latitude - segment.first.latitude
        val deltaLon = segment.second.longitude - segment.first.longitude
        val centralAngleAlongXAxis =
            segment.first.centralAngle(segment.first.plus(0.0, deltaLon))
        return SphericalToXYMapping(
            origin = segment.first,
            segment = Point(0.0, 0.0) to Point(sign(deltaLon) * centralAngleAlongXAxis, deltaLat),
        )
    }

    private fun Point.toSpherical(origin: LocationRadians): LocationRadians {
        //  y - центральный угол по меридиану
        //  x - центральный угол по параллели
        val lat = origin.latitude + y

        // L =  R_small * Ang_small = R_big * Ang_big
        // Ang_big = x
        // R_small = R_big * cos(lat)
        // Ang_small = lon- ??????
        // Ang_small = R_big * Ang_big / R_small = R_big * Ang_big / (R_big * cos(lat)) = Ang_big / cos(lat)
        val lon = origin.longitude + x / cos(lat)
        return LocationRadians(lat, lon)
    }

    private fun toSpherical(points: Pair<Point, Point>, origin: LocationRadians) =
        points.map { it.toSpherical(origin) }

    object Check {

        private fun shouldBeClose(
            arg1: Double, arg2: Double,
            arg1Name: String = "", arg2Name: String = "",
            epsilon: Double
        ) {
            if (arg1 != arg2) {
                val delta = abs(arg1 - arg2)
                if (delta > epsilon) {
                    throw Exception("$arg1Name != $arg2Name: $arg1 $arg2 $delta")
                }
            }
        }

        fun locations(
            locations: Pair<LocationRadians, LocationRadians>,
            segment: Pair<LocationRadians, LocationRadians>,
            distance: Double,
            epsilon: Double = 5e-10
        ) {
            val (n1, n2) = locations
            val (a, b) = segment

            val m = a.midpoint(b)
            val mn1 = m.centralAngle(n1)
            val mn2 = m.centralAngle(n2)
            val an1 = a.centralAngle(n1)
            val an2 = a.centralAngle(n2)
            val bn1 = b.centralAngle(n1)
            val bn2 = b.centralAngle(n2)
            val am = a.centralAngle(m)
            val bm = b.centralAngle(m)
            shouldBeClose(mn1, mn2, "mn1", "mn2", epsilon)
            shouldBeClose(mn1, distance, "mn1", "distance", epsilon)
            shouldBeClose(mn2, distance, "mn2", "distance", epsilon)

            shouldBeClose(an1, an2, "an1", "an2", epsilon)
            shouldBeClose(bn1, bn2, "bn1", "bn2", epsilon)
            shouldBeClose(an1, bn1, "an1", "bn1", epsilon)
            shouldBeClose(an2, bn2, "an2", "bn2", epsilon)

            shouldBeClose(cos(an1), cos(am) * cos(mn1), "cos(an1)", "cos(am)* cos(mn1)", epsilon)
            shouldBeClose(cos(an2), cos(am) * cos(mn2), "cos(an2)", "cos(am)* cos(mn2)", epsilon)

            shouldBeClose(cos(bn1), cos(bm) * cos(mn1), "cos(bn1)", "cos(bm)* cos(mn1)", epsilon)
            shouldBeClose(cos(bn2), cos(bm) * cos(mn2), "cos(bn2)", "cos(bm)* cos(mn2)", epsilon)
        }

        fun points(
            points: Pair<Point, Point>,
            segment: Pair<Point, Point>,
            distance: Double,
            epsilon: Double = 5e-13
        ) {
            val (n1, n2) = points
            val (a, b) = segment

            val m = a.midpoint(b)
            val mn1 = m.distance(n1)
            val mn2 = m.distance(n2)
            shouldBeClose(mn1, mn2, "mn1", "mn2", epsilon)
            shouldBeClose(mn1, distance, "mn1", "distance", epsilon)

            val an1 = a.distance(n1)
            val an2 = a.distance(n2)
            shouldBeClose(an1, an2, "an1", "an2", epsilon)

            val bn1 = b.distance(n1)
            val bn2 = b.distance(n2)
            shouldBeClose(bn1, bn2, "bn1", "bn2", epsilon)
            shouldBeClose(an1, bn1, "an1", "bn1", epsilon)
            shouldBeClose(an2, bn2, "an2", "bn2", epsilon)

            val am = a.distance(m)
            val bm = b.distance(m)
            shouldBeClose(am, bm, "am", "bm", epsilon)
            shouldBeClose(pow2(bm) + pow2(mn1), pow2(bn1), "bm^2+mn1^2", "bn1^2", epsilon)
            shouldBeClose(pow2(am) + pow2(mn1), pow2(an1), "am^2+mn1^2", "an1^2", epsilon)
        }
    }
}

operator fun PointsAtDistanceToLineSegmentMidpoint.invoke(segment: Pair<Location, Location>) =
    this(segment.map { it.toLocationRadians() })
        .map { it.toLocation() }

@JvmName("invokeGeoPoint")
operator fun PointsAtDistanceToLineSegmentMidpoint.invoke(segment: Pair<IGeoPoint, IGeoPoint>) =
    this(segment.map { it.toLocationRadians() })
        .map { it.toLocation() }