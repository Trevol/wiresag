package com.example.wiresag

import android.location.Location
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

}

private fun Any.println() = println(this)
private fun Any.println(prefix: String) = "$prefix: $this".println()