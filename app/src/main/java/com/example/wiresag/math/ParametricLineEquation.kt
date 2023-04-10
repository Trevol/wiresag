package com.example.wiresag.math

class ParametricLineEquation(val p0: Point, val direction: Vector) {
    constructor(p0: Point, k: Double) : this(p0 = p0, direction = Vector(1.0, k))
    constructor(p1: Point, p2: Point) : this(p0 = p1, direction = p2 - p1)
    constructor(twoPoints: Pair<Point, Point>) : this(twoPoints.second, twoPoints.first)

    init {
        if (direction.x == 0.0 && direction.y == 0.0) {
            throw Exception("Unexpected: direction is zero-vector")
        }
    }

    val k = direction.y / direction.x

    fun normalLine(p: Point) = ParametricLineEquation(p0 = p, k = -1 / k)

    fun intersection(eq2: ParametricLineEquation): Point? {
        val eq1 = this
        if (eq1.k == eq2.k){
            return null
        }
        TODO()
    }
}