package com.example.wiresag

import com.example.wiresag.utils.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
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