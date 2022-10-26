package com.example.sensors.utils

fun Double.round(decimals: Int): Double {
    val d = pow10(decimals)
    return kotlin.math.round(this * d) / d
}

fun Float.round(decimals: Int): Float {
    val d = pow10(decimals)
    return kotlin.math.round(this * d) / d
}

private fun pow10(n: Int): Int {
    var result = 1
    repeat(n) {
        result *= 10
    }
    return result
}