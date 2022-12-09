package com.example.wiresag

import android.location.Location
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wiresag.utils.Location
import com.example.wiresag.utils.plus

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import kotlin.math.sqrt


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    /*@Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.wiresag", appContext.packageName)
    }*/

    @Test
    fun distance() {
        val latDelta = 0.000006
        val lonDelta = 0.000006

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
                it.plus(latDelta, 0.0).distanceTo(it) to it.plus(0.0, lonDelta).distanceTo(it)
                //it.plus(latDelta, lonDelta).distanceTo(it)
            }
            .forEach { it.println("DIST") }
    }

    @Test
    fun realDistVsSimplifiedDist() {
        //val (latDelta, lonDelta) = 0.0008 to 0.0014
        val (latDelta, lonDelta) = 0.0008 to 1.014
        //val p1 = Location(45.0, 23.0)
        val p1 = Location(83.0, -23.0)
        val p2 = p1.plus(latDelta, lonDelta)
        p2.println()
        val preciseDistance = p1.distanceTo(p2)
        preciseDistance.println("DIST (PRECISE)")
        val simplifiedDistance = p1.simplifiedDistanceTo(p2)
        simplifiedDistance.println("DIST (SIMPLIF)")
        val distanceDelta = preciseDistance - simplifiedDistance
        distanceDelta.println("DIST DELTA")
    }

}

private fun Location.simplifiedDistanceTo(other: Location): Float {
    val p1 = this
    val p2 = other
    val p0 = Location(p2.latitude, p1.longitude)
    val p0p1 = p0.distanceTo(p1)
    val p0p2 = p0.distanceTo(p2)
    val p1p2 = sqrt(p0p1 * p0p1 + p0p2 * p0p2)
    return p1p2
}
