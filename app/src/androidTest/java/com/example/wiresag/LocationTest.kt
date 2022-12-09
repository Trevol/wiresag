package com.example.wiresag

import android.location.Location
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wiresag.utils.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*


@RunWith(AndroidJUnit4::class)
class LocationTest {
    @Test
    fun selfComputedDistance() {
        val latDelta = 0.00006
        val lonDelta = 0.0006

        listOf(
            Location(0.0, 0.12),
            Location(45.0, 23.12),
            Location(-89.0, 23.12),
            /*Location(89.0, 43.12),

            Location(89.9, 23.12),
            Location(89.9, 43.12),

            Location(46.12373213, 23.12373213),
            Location(0.12373213, -10.12373213)*/
        )
            .map {
                //it.plus(latDelta, 0.0).distanceTo(it) to it.plus(0.0, lonDelta).distanceTo(it)
                val other = it.plus(latDelta, lonDelta)
                other.distanceTo(it) to other.bigCircleDistanceTo(it)
            }
            .forEach { it.println("DIST") }
    }

    @Test
    fun midpointTest() {

        fun Location.toDisplayString() =
            "$latitude, $longitude (${DMS(latitude).prettyFormat()}, ${DMS(longitude).prettyFormat()})"

        val latDelta = 0.0003
        val lonDelta = 0.0014

        listOf(
            Location(0.0, 0.0) to Location(0.0, 2.0),
            Location(0.0, 0.0) to Location(2.0, 0.0),
            Location(46.0, 23.0) to Location(46.5, 23.8),
            Location(46.5, 23.8) to Location(46.0, 23.0),
            Location(46.5, 23.0) to Location(46.5, 23.8),

            Location(46.5, 23.0).let { it to it.plus(latDelta, lonDelta) }
        ).map { (p1, p2) ->
            val mp1 = p1.midpoint(p2)
            val mp2 = p1.soSimplifiedMidpoint(p2)
            Triple(mp1, mp2, p1.distanceTo(p2) to mp1.distanceTo(mp2))

        }.forEach { (mp1, mp2, distances) ->
            mp1.toDisplayString().println("1")
            mp2.toDisplayString().println("2")
            distances.println("D")
            "------".println()
        }
    }

    @Test
    fun out() {

    }
}


