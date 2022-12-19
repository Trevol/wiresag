package math

import kotlin.math.round

fun Double.round(decimals: Int): Double {
    val d = pow10(decimals)
    return round(this * d) / d
}

private fun pow10(n: Int): Int {
    var result = 1
    repeat(n) {
        result *= 10
    }
    return result
}

inline fun pow2(a: Double) = a * a
inline fun pow2(a: Float) = a * a
inline fun squared(a: Double) = pow2(a)
inline fun squared(a: Float) = pow2(a)

inline fun sign(a: Double) = if (a < 0) -1.0 else 1.0
inline fun sign(a: Int) = if (a < 0) -1 else 1