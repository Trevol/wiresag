package com.example.wiresag.math

import androidx.compose.ui.geometry.Offset

class Parabola(val a: Float, val b: Float, val c: Float) {
    val vertex = (b * b - 4 * a * c).let { d -> Offset(-b / (2 * a), -d / (4 * a)) }
    operator fun invoke(x: Float): Float {
        return a * x * x + b * x + c
    }
}

fun Parabola(p1: Offset, p2: Offset, p3: Offset): Parabola {
    val a = (p3.y - (p3.x * (p2.y - p1.y) + p2.x * p1.y - p1.x * p2.y) / (p2.x - p1.x)) /
            (p3.x * (p3.x - p1.x - p2.x) + p1.x * p2.x)
    val b = (p2.y - p1.y) / (p2.x - p1.x) - a * (p1.x + p2.x)
    val c = (p2.x * p1.y - p1.x * p2.y) / (p2.x - p1.x) + a * p1.x * p2.x

    return Parabola(a, b, c)
}