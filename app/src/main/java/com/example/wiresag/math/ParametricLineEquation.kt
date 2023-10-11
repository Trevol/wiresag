package com.example.wiresag.math

class ParametricLineEquation(val p0: Point2d, val v: Vector2d) {
    constructor(p0: Point2d, k: Double) : this(p0 = p0, v = Vector2d(1.0, k))
    constructor(p1: Point2d, p2: Point2d) : this(p0 = p1, v = p2 - p1)
    constructor(twoPoints: Pair<Point2d, Point2d>) : this(twoPoints.second, twoPoints.first)

    init {
        if (v.isZeroVector) {
            throw Exception("Unexpected: direction is zero-vector")
        }
    }

    val k = v.y / v.x

    fun x(t: Double) = p0.x + v.x * t

    fun y(t: Double) = p0.y + v.y * t

    fun point(t: Double) = p0 + v * t

    // Find t by known x
    fun tByX(x: Double) = (x - p0.x) / v.x

    // Find t by known y
    fun tByY(y: Double) = (y - p0.y) / v.y

    fun normalLine(p: Point2d) = ParametricLineEquation(p0 = p, k = -1 / k)

    fun intersection(eq2: ParametricLineEquation): Point2d? {
        val eq1 = this
        if (eq1.v.isCollinear(eq2.v)) {
            return null
        }

        // edges cases
        /*if (eq1.v.x == .0) {
            val x = eq1.p0.x
            return Point2d(
                x = x,
                y = eq2.y(t = eq2.tByX(x))
            )
        }
        if (eq1.v.y == .0) {
            val y = eq1.p0.y
            return Point2d(
                x = eq2.x(t = eq2.tByY(y)),
                y = y
            )
        }
        if (eq2.v.x == .0) {
            val x = eq2.p0.x
            return Point2d(
                x = x,
                y = eq1.y(t = eq1.tByX(x))
            )
        }
        if (eq2.v.y == .0) {
            val y = eq2.p0.y
            return Point2d(
                x = eq1.x(t = eq1.tByY(y)),
                y = y
            )
        }*/

        val t2 = (eq1.v.x * (eq1.p0.y - eq2.p0.y) + eq1.v.y * (eq2.p0.x - eq1.p0.x)) /
                (eq1.v.x * eq2.v.y - eq1.v.y * eq2.v.x)
        val x = eq2.x(t2)
        val y = eq2.y(t2)
        return Point2d(x, y)
    }
}