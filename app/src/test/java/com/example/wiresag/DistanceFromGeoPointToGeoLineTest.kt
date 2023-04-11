package com.example.wiresag

import com.example.wiresag.math.LineEquation
import com.example.wiresag.math.ParametricLineEquation
import com.example.wiresag.math.Point2d
import com.example.wiresag.math.Vector2d
import io.kotest.matchers.shouldBe
import org.junit.Test

class DistanceFromGeoPointToGeoLineTest {
    @Test
    fun distToLine() {
        val spanLine = LineEquation(Point2d(0.0, 0.0), Point2d(2.0, 0.0))
        val location = Point2d(1.0, 1.0)
        val normalToSpanLine = spanLine.normalLine(location)
        normalToSpanLine
            .let {
                it.k.println("K")
                it.intersection(spanLine)!!.println("Normal to span base")
            }
    }

    @Test
    fun lineIntersectionTest() {

        (LineEquation(.5, 1.0) to LineEquation(-.5, 2.0))
            .let { (l1, l2) ->
                val expected = Point2d(1.0, 1.5)
                l1.intersection(l2) shouldBe expected
                l2.intersection(l1) shouldBe expected
            }

        (LineEquation(.5, 1.0) to LineEquation(.5, 2.0))
            .let { (l1, l2) ->
                l1.intersection(l2) shouldBe null
                l2.intersection(l1) shouldBe null
            }

        (LineEquation(0.0, 1.0) to LineEquation(-.5, 2.0))
            .let { (l1, l2) ->
                val expected = Point2d(2.0, 1.0)
                l1.intersection(l2) shouldBe expected
                l2.intersection(l1) shouldBe expected
            }

        //x=1, y=1
        /*(LineEquation(Point(1.0, 0.0), Point(1.0, 3.0)) to LineEquation(0.0, 1.0))
            .let { (l1, l2)->
                val expected = Point(1.0, 1.0)
                l1.intersection(l2) shouldBe expected
                l1.intersection(l2) shouldBe expected
            }*/
    }

    @Test
    fun parametricLineEquationTest() {
        ParametricLineEquation(Point2d(12.56, -4.67), k = .1234).k shouldBe .1234
        ParametricLineEquation(
            Point2d(1.0, 0.0),
            Vector2d(0.0, 1.0)
        ).k shouldBe Double.POSITIVE_INFINITY
    }
}