package com.example.wiresag.utils

import kotlin.math.*

fun Int.zeroPad(length: Int) = toString().padStart(length, '0')
fun Double.toFixed(fractionDigits: Int) = 10.0.pow(fractionDigits)
    .let { scale-> round(this * scale) / scale }
fun Float.toFixed(fractionDigits: Int) = 10.0.pow(fractionDigits)
    .let { scale-> round(this * scale) / scale }.toFloat()
inline fun Double.fraction() = abs(this).let { it - floor(it) }
