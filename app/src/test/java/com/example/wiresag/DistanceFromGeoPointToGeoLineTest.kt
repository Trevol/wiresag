package com.example.wiresag

import com.example.wiresag.math.LineEquation
import com.example.wiresag.math.Point
import io.kotest.matchers.shouldBe
import org.junit.Test

class DistanceFromGeoPointToGeoLineTest {
    @Test
    fun distToLine() {
        val spanLine = LineEquation(Point(0.0, 0.0), Point(2.0, 0.0))
        val location = Point(1.0, 1.0)
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
                val expected = Point(1.0, 1.5)
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
                val expected = Point(2.0, 1.0)
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
}