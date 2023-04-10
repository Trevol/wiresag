package com.example.wiresag.math

import kotlin.math.sqrt

data class Point(val x: Double, val y: Double)

operator fun Point.plus(vector: Vector) = Point(x + vector.x, y + vector.y)
operator fun Point.minus(start: Point) = Vector(x - start.x, y - start.y)

fun Point(x: Int, y: Int) = Point(x.toDouble(), y.toDouble())

fun Point.midpoint(other: Point) = Point((x + other.x) / 2, (y + other.y) / 2)

inline fun Point.squareDistance(other: Point) = pow2(x - other.x) + pow2(y - other.y)
fun Point.distance(other: Point) = sqrt(squareDistance(other))
