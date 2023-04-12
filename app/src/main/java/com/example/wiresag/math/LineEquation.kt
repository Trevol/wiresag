package com.example.wiresag.math

/*

class LineEquation(val k: Double, private val y0: Double) {
    constructor(k: Double, p: Point2d) : this(k = k, y0 = p.y - p.x * k)
    constructor(p1: Point2d, p2: Point2d) : this(k = k(p2, p1), p1)
    constructor(twoPoints: Pair<Point2d, Point2d>) : this(twoPoints.second, twoPoints.first)

    init {
        if (k.isNaN()) {
            throw Exception("Unexpected: k.isNaN")
        }
    }

    operator fun invoke(x: Double): Double = y(x)
    private fun y(x: Double) = k * x + y0

    fun normalLine(p: Point2d): LineEquation {
        // уравнение прямой, проходящей через точку p и перпендикулярной данной прямой,
        // tan(a+90) = -cot(a) = -1/tan(a)
        return LineEquation(k = -1 / k, p = p)
    }

    fun intersection(eq2: LineEquation): Point2d? {
        //знаменатель не должен быть 0. Подбираем (eq1, eq2) так, что бы eq1.k != 0
        val (eq1, eq2) = if (k == 0.0) {
            eq2 to this
        } else {
            this to eq2
        }

        if (eq1.k == eq2.k) {
            return null
        }

        val k2_to_k1 = eq2.k / eq1.k
        val y = (eq2.y0 - k2_to_k1 * eq1.y0) / (1 - k2_to_k1)
        val x = (y - eq1.y0) / eq1.k
        return Point2d(x, y)
    }

    companion object {
        private fun k(p1: Point2d, p2: Point2d) = (p2.y - p1.y) / (p2.x - p1.x)
    }
}
*/

