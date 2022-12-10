package com.example.wiresag.math

import math.Point

class LineEquation(val k: Double, val y0: Double) {
    constructor(k: Double, p: Point) : this(k = k, y0 = p.y - p.x * k)
    constructor(p1: Point, p2: Point) : this(k = k(p2, p1), p1)
    constructor(segment: Pair<Point, Point>) : this(segment.second, segment.first)

    init {
        if (k.isNaN()) {
            throw Exception("Unexpected: k.isNaN")
        }
    }

    inline operator fun invoke(x: Double): Double = y(x)
    inline operator fun invoke(x: Int): Double = y(x)

    fun y(x: Double) = k * x + y0
    fun y(x: Int) = y(x.toDouble())

    fun normalLine(p: Point): LineEquation {
        // уравнение прямой, проходящей через т. p и перпендикулярной данной прамой,
        // tan(a+90) = -cot(a) = -1/tan(a)
        return LineEquation(k = -1 / k, p = p)
    }

    companion object {
        fun k(p1: Point, p2: Point) = (p2.y - p1.y) / (p2.x - p1.x)
    }
}