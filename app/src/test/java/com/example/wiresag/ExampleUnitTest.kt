package com.example.wiresag

import org.junit.Test

class ExampleUnitTest {

    @Test
    fun prettyFormat() {

    }

}

private fun Any.println() = println(this)
private fun Any.println(prefix: String) = "$prefix: $this".println()