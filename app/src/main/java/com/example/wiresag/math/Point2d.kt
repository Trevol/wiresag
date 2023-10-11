package com.example.wiresag.math

import kotlin.math.sqrt

data class Point2d(val x: Double, val y: Double)

operator fun Point2d.plus(vector: Vector2d) = Point2d(x + vector.x, y + vector.y)
operator fun Point2d.minus(start: Point2d) = Vector2d(x - start.x, y - start.y)

fun Point2d(x: Int, y: Int) = Point2d(x.toDouble(), y.toDouble())

fun Point2d.midpoint(other: Point2d) = Point2d((x + other.x) / 2, (y + other.y) / 2)

inline fun Point2d.squareDistance(other: Point2d) = pow2(x - other.x) + pow2(y - other.y)
fun Point2d.distance(other: Point2d) = sqrt(squareDistance(other))
