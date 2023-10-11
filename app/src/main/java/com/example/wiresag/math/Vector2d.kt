package com.example.wiresag.math

import kotlin.math.abs

data class Vector2d(val x: Double, val y: Double)

operator fun Vector2d.times(t: Double) = Vector2d(x * t, y * t)

val Vector2d.isZeroVector get() = x == 0.0 && y == 0.0

fun Vector2d.isCollinear(b: Vector2d) =
    (x == .0 && b.x == .0) ||
            (y == .0 && b.y == .0) ||
            x / b.x == y / b.y ||
            (b.x / x).equalOrCloseTo(b.y / y)

private const val EPS = 1e-15

private inline fun Double.equalOrCloseTo(other: Double, eps: Double = EPS): Boolean {
    return this == other || abs(this - other) <= eps
}