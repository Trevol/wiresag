package com.example.wiresag

import android.location.Location
import com.example.wiresag.utils.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun prettyFormat() {
        val latitude = 46.12373213
        val longitude = -23.12373213

        DMS(latitude).prettyFormat().let {
            println(it)
        }

        DMS(longitude).prettyFormat().let {
            println(it)
        }
    }

    @Test
    fun distance() {

        val latDelta = 0.003
        val lonDelta = 0.001

        val loc1 = Location("dummy").apply {
            latitude = 46.12373213
            longitude = 23.12373213
        }

        val loc12 = Location("dummy").apply {
            latitude = loc1.latitude + latDelta
            longitude = loc1.longitude + lonDelta
        }

        loc1.distanceTo(loc12).println()
    }
}

private fun Any.println() = println(this)