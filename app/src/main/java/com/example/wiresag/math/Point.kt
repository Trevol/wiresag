package math

import kotlin.math.sqrt

data class Point(val x: Double, val y: Double)

fun Point(x: Int, y: Int) = Point(x.toDouble(), y.toDouble())

fun Point.midpoint(other: Point) = Point((x + other.x) / 2, (y + other.y) / 2)
fun Point.distance(other: Point) = sqrt(pow2(x - other.x) + pow2(y - other.y))