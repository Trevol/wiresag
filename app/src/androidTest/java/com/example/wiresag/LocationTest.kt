package com.example.wiresag

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wiresag.location.toLocation
import com.example.wiresag.location.toLocationRadians
import com.example.wiresag.math.PointsAtDistanceToLineSegmentMidpoint
import com.example.wiresag.math.geo.Earth
import com.example.wiresag.math.invoke
import com.example.wiresag.utils.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationTest {
    @Test
    fun out() {
        val angularDistToSpan = 20.0 / Earth.r
        val locationsWithDistToSpan =
            PointsAtDistanceToLineSegmentMidpoint(angularDistanceToMidpoint = angularDistToSpan)

        val spans = listOf(
            Location(45.0, 23.0) to Location(45.0, 23.00078),
            Location(45.0, 23.00078) to Location(45.0, 23.0),

            Location(45.0, 23.0) to Location(45.00078, 23.0),
            Location(45.00078, 23.0) to Location(45.0, 23.0),

            Location(45.0, 23.0) to Location(45.00078, 23.00078),
            Location(45.00078, 23.00078) to Location(45.0, 23.0),

            Location(45 - 0.00078, 23.0) to Location(45.00078, 23.00078),
            Location(45.00078, 23.00078) to Location(45 - 0.00078, 23.0),

            // Location(45.0, 23.0) to Location(45.0078, 23.0078)
        )

        spans.forEach { (pylon1, pylon2) ->
            val segment = pylon1.toLocationRadians() to pylon2.toLocationRadians()

            val result = locationsWithDistToSpan.extendedInvoke(segment)

            PointsAtDistanceToLineSegmentMidpoint.Check.points(
                result.points,
                result.segmentIn2D,
                angularDistToSpan
            )
            PointsAtDistanceToLineSegmentMidpoint.Check.locations(
                result.locations,
                segment,
                angularDistToSpan
            )

            val resultAsLocations = locationsWithDistToSpan(pylon1 to pylon2)

            PointsAtDistanceToLineSegmentMidpoint.Check.locations(
                resultAsLocations.map { it.toLocationRadians() },
                segment,
                angularDistToSpan
            )

            val (n1, n2) = result.locations.toList().map { it.toLocation() }
            "DistTo: ${n1.distanceTo(n2)}".println()
        }

    }
}


