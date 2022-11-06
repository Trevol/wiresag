package com.example.wiresag

import android.location.Location
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.wiresag", appContext.packageName)
    }

    @Test
    fun distance() {
        val latDelta = 0.000006
        val lonDelta = 0.000007

        Location("dummy").apply {
            latitude = 46.12373213
            longitude = 23.12373213
        }.let { loc ->
            val loc2 = Location("dummy").apply {
                latitude = loc.latitude + latDelta
                longitude = loc.longitude + lonDelta
            }
            loc.distanceTo(loc2) to loc2.distanceTo(loc)
        }.println("DIST")


        val loc2 = Location("dummy").apply {
            latitude = 0.12373213
            longitude = -10.12373213
        }

        val loc22 = Location("dummy").apply {
            latitude = loc2.latitude + latDelta
            longitude = loc2.longitude + lonDelta
        }
        loc2.distanceTo(loc22).println("DIST")
        loc22.distanceTo(loc2).println("DIST")
    }

    private fun Any.println() = println(this)
    private fun Any.println(prefix: String) = "$prefix: $this".println()
}