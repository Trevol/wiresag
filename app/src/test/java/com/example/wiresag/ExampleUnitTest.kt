package com.example.wiresag

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun prettyFormat() {
        val d: Deferred<Int>

        suspend fun getData(): Int {
            TODO()
        }

        suspend fun getNullableData(): Int? {
            TODO()
        }

        runBlocking {
            deferredResult { getData() }
            deferredResult { getNullableData() }
        }
    }

}

data class DeferredResult<T>(val value: T?, val completed: Boolean)

suspend fun <T> deferredResult(result: suspend () -> T?): DeferredResult<T> {
    val value = result()
    DeferredResult(value, true)
    TODO()
}