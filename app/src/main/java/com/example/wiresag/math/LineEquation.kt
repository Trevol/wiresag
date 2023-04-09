package com.example.wiresag.math

class LineEquation(val k: Double, val y0: Double) {
    constructor(k: Double, p: Point) : this(k = k, y0 = p.y - p.x * k)
    constructor(p1: Point, p2: Point) : this(k = k(p2, p1), p1)
    constructor(twoPoints: Pair<Point, Point>) : this(twoPoints.second, twoPoints.first)

    init {
        if (k.isNaN()) {
            throw Exception("Unexpected: k.isNaN")
        }
    }

    inline operator fun invoke(x: Double): Double = y(x)
    //inline operator fun invoke(x: Int): Double = y(x)

    fun y(x: Double) = k * x + y0
    //fun y(x: Int) = y(x.toDouble())

    fun normalLine(p: Point): LineEquation {
        // уравнение прямой, проходящей через т. p и перпендикулярной данной прамой,
        // tan(a+90) = -cot(a) = -1/tan(a)
        return LineEquation(k = -1 / k, p = p)
    }

    fun intersection(eq2: LineEquation): Point? {
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
        return Point(x, y)
    }

    companion object {
        fun k(p1: Point, p2: Point) = (p2.y - p1.y) / (p2.x - p1.x)
    }
}